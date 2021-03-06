package com.aceucv.vpe.crawler.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.aceucv.vpe.crawler.entities.Category;
import com.aceucv.vpe.crawler.entities.Offer;
import com.aceucv.vpe.crawler.model.Resources;

/**
 * Main GUI Frame which holds all data to be crawled
 * Also has a separate tab for setting preferences
 * Must be set visible. Is self-
 *
 * @author cristiantotolin
 *
 */
public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	// Pane for the tabs
	private JTabbedPane tabs;

	// Objects in 1st tab (Offers tab)
	public JButton buttonOpenOffer;
	public JButton buttonDeleteOffer;
	public JButton buttonSelectAllOffers;
	public JButton buttonDeselectAllOffers;

	public JPanel sidePanelOffers;
	public JLabel itemsLabel;
	public JLabel selectionOffersLabel;
	public JLabel logo;

	// Objects in 2nd tab (Settings tab)
	public JButton buttonCrawlItems;
	public JButton buttonCrawlCategories;
	public JButton buttonSelectAllCategories;
	public JButton buttonDeselectAllCategories;
	public JButton buttonSaveChanges;
	public JPanel sidePanelCategories;

	private JPanel  buttonPanelItems;
	private JPanel  buttonPanelCategories;
	public  boolean allSelected = false;
	public  JTable  itemsTable;
	public  JTable  categoriesTable;
	public  JLabel  settingsLabel;
	private JScrollPane  paneItems;
	private JScrollPane  paneCategories;
	public  JProgressBar progressCategories;
	private JFrame frame = new JFrame();

	private String[] columnNamesItems = { "Name", "ID", "Price (RON)", "Discount (RON)"};
	private String[] columnNamesCategories = {"ID", "Name", "Select" };

	private Object[][] dataOffers = {};
	private Object[][] dataCategories = {};

	private DefaultTableModel modelItems = new DefaultTableModel(dataOffers, columnNamesItems) {
		private static final long serialVersionUID = 1L;
	};

	private DefaultTableModel modelSettings = new DefaultTableModel(dataCategories, columnNamesCategories) {
		private static final long serialVersionUID = 1L;

		@Override
		public Class<?> getColumnClass(int column) {
			switch (column) {
			case 2:
				return Boolean.class;
			default:
				return String.class;
			}
		}
	};

	public void addItemToList(Offer newOffer) {
		Object[] data = { newOffer.getName(), newOffer.getCategory(), newOffer.getPriceString(), newOffer.getDiscountString(),
				false };

		modelItems.addRow(data);
	}

	public void addCategoryToList(Category newCategory) {
		Object[] data = { newCategory.getId(), newCategory.getDescription(), false };
		System.out.println("Adding new category");
		modelSettings.addRow(data);
	}
	
	public void populateCategoryList(List<Category> categories) {
		clearCategoryList();
		if (categories == null) {
			// TODO DISPLAY AN EROR
			return;
		}
		
		for (Category category : categories) {
			Object[] data = { category.getId(), category.getDescription(), true };
			modelSettings.addRow(data);
		}
	}
	
	public void populateOfferList(List<Offer> offers) {
		clearItemsList();
		
		for (Offer offer : offers) {
			Object[] data = { offer.getName(), 
					offer.getIdString(),
					offer.getPriceString(), 
					offer.getDiscountString() };
			modelItems.addRow(data);
		}
	}
	
	public Offer getSelectedItem() {
		int selected[] = itemsTable.getSelectedRows();
		if (selected.length > 0) {
			int currently_selected = selected[0];
			
			int id = Integer.parseInt((String) 
					modelItems.getValueAt(currently_selected, 1));

			if (Resources.offers.containsKey(id)){
				return Resources.offers.get(id);
			}
		}
		
		return null;
	}
	
	public List<Integer> getSelectedItemsIndex() {
		int selected[] = itemsTable.getSelectedRows();
		List<Integer> offers = new ArrayList<Integer>();
		if (selected.length > 0) {
			for (int i=0; i<selected.length; i++) {
				int currently_selected = selected[i];
				
				int id = Integer.parseInt((String) 
						modelItems.getValueAt(currently_selected, 1));

				if (Resources.offers.containsKey(id)){
					offers.add(id);
				}
				
				modelItems.removeRow(i);
			}
		}
		
		return offers;
	}
	
	public void clearCategoryList() {
		modelSettings.setRowCount(0);
	}
	
	public void clearLoadingBar() {
		progressCategories.setValue(0);
	}
	
	public void clearItemsList() {
		modelItems.setRowCount(0);
	}
	
	public void toggleSelectionCategories(boolean value) {
		int nRow = modelSettings.getRowCount();
		
		for (int i = 0 ; i < nRow ; i++) {
	    	modelSettings.setValueAt(value, i, 2);
	    }
	}
	
	public void toggleSelectionOffers(boolean value) {
		if (!value) {
			itemsTable.clearSelection();
		} else {
			itemsTable.setRowSelectionInterval(0, itemsTable.getRowCount()-1);
		}
	}
	
	public void incrementProgress(int value) {
		this.progressCategories.setValue(
				this.progressCategories.getValue()
				+value
				);
	}
	
	public List<Integer> getSelectedCategories() {
	    int nRow = modelSettings.getRowCount();
		List<Integer> indexSelected = new ArrayList<Integer>();
	   	boolean selected;
	   	Integer value;
	   	
	    // For each row, check if selected
	    for (int i = 0 ; i < nRow ; i++) {
	    	selected = (Boolean) modelSettings.getValueAt(i, 2);
	    	if (selected) {
	    		value = (Integer) modelSettings.getValueAt(i, 0);
	    		indexSelected.add(value);
	    	}
	    }
	    
	    return indexSelected;
	}

	private JPanel getItemsTab() {
		// Create the container (main)
		JPanel container = new JPanel();

		// Create the table
		itemsTable = new JTable(modelItems);

		// Create all the buttons and labels needed
		buttonOpenOffer = new JButton("    Open Offer    ");
		buttonDeleteOffer = new JButton("   Delete Offer   ");
		buttonSelectAllOffers = new JButton("    Select all    ");
		buttonDeselectAllOffers = new JButton("  Deselect all  ");
		itemsLabel = new JLabel(Resources.label_text_offers);
		selectionOffersLabel = new JLabel(Resources.label_text_selection_offers);
		logo = getLogo();

		// Set alignments
		itemsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		selectionOffersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonOpenOffer.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonDeleteOffer.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonSelectAllOffers.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonDeselectAllOffers.setAlignmentX(Component.CENTER_ALIGNMENT);
		logo.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Add the buttons to a button panel
		buttonPanelItems = new JPanel();
		buttonPanelItems.setLayout(new BoxLayout(buttonPanelItems, BoxLayout.Y_AXIS));

		addPadding(buttonPanelItems, 2);
		buttonPanelItems.add(logo);

		addPadding(buttonPanelItems, 3);
		buttonPanelItems.add(itemsLabel);

		addPadding(buttonPanelItems, 1);
		buttonPanelItems.add(buttonOpenOffer);
		buttonPanelItems.add(buttonDeleteOffer);

		addPadding(buttonPanelItems, 3);
		buttonPanelItems.add(selectionOffersLabel);
		addPadding(buttonPanelItems, 1);
		buttonPanelItems.add(buttonSelectAllOffers);
		buttonPanelItems.add(buttonDeselectAllOffers);

		// Create a scrollable pane for the list
		paneItems = new JScrollPane(itemsTable);

		// Add items via layout
		container.setLayout(new BorderLayout());
		container.add(buttonPanelItems, BorderLayout.WEST);
		// container.add(testpanel, BorderLayout.WEST);
		container.add(paneItems, BorderLayout.CENTER);

		return container;
	}

	private JPanel getSettingsTab() {
		// Create the container (main)
		JPanel container = new JPanel();

		// Create the table
		categoriesTable = new JTable(modelSettings);

		// Create all the buttons needed
		buttonCrawlCategories = new JButton("Search");
		buttonSelectAllCategories = new JButton("Select/Deselect");
		buttonCrawlItems = new JButton("Crawl");
		buttonSaveChanges = new JButton("Save");
		settingsLabel = new JLabel(Resources.label_text_settings);

		// Create a progress bar for crawling progress
		progressCategories = new JProgressBar();

		// Add the buttons to a button panel
		buttonPanelCategories = new JPanel();
		buttonPanelCategories.add(buttonCrawlCategories);
		buttonPanelCategories.add(buttonCrawlItems);
		buttonPanelCategories.add(settingsLabel);
		buttonPanelCategories.add(progressCategories);
		buttonPanelCategories.add(buttonSelectAllCategories);
		buttonPanelCategories.add(buttonSaveChanges);

		// Create a scrollable pane for the list
		paneCategories = new JScrollPane(categoriesTable);

		// Add items via layout
		container.setLayout(new BorderLayout());
		container.add(buttonPanelCategories, BorderLayout.NORTH);
		container.add(paneCategories, BorderLayout.CENTER);

		return container;
	}

	// Adds a blank padding in the current location of the JPanel
	public void addPadding(JPanel panel, int count) {
		while (count > 0) {
			count--;
			panel.add(new JLabel(" "));
		}
	}

	private JLabel getLogo() {
		// Read the image through a buffer
		BufferedImage logoPicture = null;
		try {
			logoPicture = ImageIO.read(new File("logo.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Resize the image to desired size
		Image dimg = logoPicture.getScaledInstance(150, 100, Image.SCALE_SMOOTH);

		// Return the label with containing picture
		JLabel picLabel = new JLabel(new ImageIcon(dimg));
		return picLabel;
	}

	public MainWindow() {
		// Current tabbed view
		tabs = new JTabbedPane();

		// Create all the tabs
		tabs.addTab("Items", getItemsTab());
		tabs.addTab("Settings", getSettingsTab());
	}
	
	public void showWindow() {
		// Set window configurations and visibility
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(tabs);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}