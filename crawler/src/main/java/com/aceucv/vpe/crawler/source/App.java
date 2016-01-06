package com.aceucv.vpe.crawler.source;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.aceucv.vpe.crawler.database.DatabaseManager;
import com.aceucv.vpe.crawler.engine.Crawler;
import com.aceucv.vpe.crawler.entities.Category;
import com.aceucv.vpe.crawler.gui.MainWindow;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws IOException {
//		Crawler crawler = new Crawler();
//		MainWindow window = new MainWindow();
//		 Map<Integer, Category> categories =
//		 crawler.crawlCategories("http://www.emag.ro/homepage",
//		 "http://emag.ro", window);
//		
//		 categories.get(12).processItems();
//		 List<Item> items = Crawler.crawlItems(categories.get(12));
//		
//		 for (Item item : items) {
//		 Crawler.setPrices(item);
//		 }

		MainWindow window = new MainWindow();
		Crawler crawler = new Crawler();
		DatabaseManager mgr = new DatabaseManager();
		Controller controller = new Controller(window, crawler, mgr);
	}
}
