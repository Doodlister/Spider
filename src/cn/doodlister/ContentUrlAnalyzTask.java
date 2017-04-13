package cn.doodlister;

import java.io.IOException;
import java.util.Stack;
import java.util.concurrent.BlockingQueue;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import cn.doodlister.Entity;

public class ContentUrlAnalyzTask implements Runnable{
/*
 * 爬去内容页面*/
	
	private BlockingQueue<String> queue;
	private Thread EUATask;
	private int i;
	private String userAgetn="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31";
	public ContentUrlAnalyzTask(BlockingQueue<String> queue,Thread EUATask,int i){
		this.queue=queue;
		this.EUATask=EUATask;
		this.i=i;
		System.out.println("线程: "+i+"开启");
	}
	@Override
	public void run() {
		Dao dao=new Dao();
		Entity ent=new Entity();
		while(EUATask.isAlive()||!queue.isEmpty()){
			try {
				
				String url=queue.take();
				Document doc;
				try {
					doc = Jsoup.connect(url).userAgent(userAgetn).get();
					Elements titleEles=doc.select("div.article-metadata-container > h1");
					ent.setTitle(titleEles.get(0).text());
					Elements abstractEles=doc.select("li.meta-pub-desc > p");
					ent.setTheAbstract(abstractEles.get(1).text());
					//存入数据库
					dao.setEntity(ent);
					System.out.println("线程: "+i+" 爬取 "+url+"完成 已存入数据库");
				} catch (HttpStatusException e1) {
					System.out.println(url+"被限制了 等待重新爬去");
					if(e1.getStatusCode()==500){//被反爬限制了
						//url 重入
						queue.put(url);
						//等10秒
						Thread.sleep(60000);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
}
