package cn.doodlister;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Stack;
import java.util.concurrent.BlockingQueue;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class EntranceUrlArchieveTask implements Runnable {
	/*
	 * 入口Url获取任务
	 * 主要功能是顺序爬取Next链接的Url并存入到阻塞队列中*/
	
	 private BlockingQueue<String> queue;
	 private Stack<String> urlStack; //用栈存储 入口Url
	 private String userAgetn="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31";
	 public EntranceUrlArchieveTask(BlockingQueue<String> queue,String entranceUrl){
		this.queue=queue;
		urlStack=new Stack<String>();
		urlStack.push(entranceUrl);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Document doc=null;
		String url="";
		while(!urlStack.isEmpty()){
			url=urlStack.pop();
			System.out.println(url);
			try {
				doc = Jsoup.connect(url).userAgent(userAgetn).get();
				
				//找到ContentUrl
				Elements contentElements=doc.getElementsByClass("browseItemTitle list-group-item-heading");
				for(Element e:contentElements){
					//找到了所有的内容页面网址 加入队列
					try {
						String DECodeUrl="http://ijs.mbr.pub2web.ingenta.com"+e.child(0).attr("href");
						DECodeUrl=java.net.URLDecoder.decode(DECodeUrl,"GBK");
						queue.put(DECodeUrl);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				
				//找到Next入口地址 入栈
				Elements nextUrlElements=doc.select("a:contains(Next >)");
				if(nextUrlElements.size()!=0){
					String DECodeUrl="http://ijs.mbr.pub2web.ingenta.com"+nextUrlElements.get(0).attr("href");
					DECodeUrl=java.net.URLDecoder.decode(DECodeUrl,"GBK");
					urlStack.push(DECodeUrl);
				}
			}catch (HttpStatusException e1) {
				System.out.println(url+"被限制了 等待重新爬取");
				if(e1.getStatusCode()==500){//被反爬限制了
					
					try {
						//url 重入
						urlStack.push(url);
						//等30秒
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}  catch (IOException e) {
				//这里可能会报 UnConnectionException 目测是因为网不好 造成的 所以需要重新入栈
				try {
					url=java.net.URLDecoder.decode(url,"GBK");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				urlStack.push(url);
				e.printStackTrace();
			}
		}
			
		
	}

}
