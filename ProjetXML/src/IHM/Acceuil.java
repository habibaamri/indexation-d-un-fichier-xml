package IHM;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import org.apache.lucene.queryParser.ParseException;


public class Acceuil extends JFrame{
	
	LuceneTester tester = new LuceneTester();
	LuceneSearch testSearch = new  LuceneSearch();

	FlowLayout flow = new FlowLayout(); 
	LineNumberReader lnr = null;
	
	JPanel panneau = new JPanel(flow);
	JPanel list = new JPanel(flow);
	JPanel panneau1 = new JPanel(flow);

	Font font = new Font("Gotham", Font.BOLD|Font.CENTER_BASELINE, 20);
	Font fontl = new Font("Serif", Font.BOLD, 15);
	
	JLabel labelName = new JLabel();
	JTextField textField1 = new JTextField(40);
    final JTextArea edit = new JTextArea(40,108);
    JButton Ouvrire = new JButton("Ouvrir",new ImageIcon("open2.png"));
    JButton Indexere = new JButton("Indexer",new ImageIcon("Edit-Text2.png"));
	JButton Rechercherr = new JButton("Rechercher",new ImageIcon("edit_zoomin.png"));
	
	JFileChooser parcourFichier;
	JMenuBar bar = new JMenuBar();
	JMenu menu = new JMenu("Fichier");
	JMenu menu1 = new JMenu("Index");
	JMenu menu2 = new JMenu("Recherche");
	JMenuItem Ouvrir= new JMenuItem("Ouvrir",new ImageIcon("fichier_open.png"));
	JMenuItem Indexer = new JMenuItem("Indexer",new ImageIcon("Edit-Text2.png"));
	JMenuItem Fermer = new JMenuItem("Fermer",new ImageIcon("fichier_quit.png"));
	JMenuItem Rechercher = new JMenuItem("Rechercher",new ImageIcon("edit_zoomin.png"));
	
	public  Acceuil(){

		//Menu
		bar.add(menu);
		menu.add(Ouvrir);
		menu.addSeparator();
		menu.add(Fermer);
		bar.add(menu1);
		menu1.add(Indexer);
		bar.add(menu2);
		menu2.add(Rechercher);
		setJMenuBar(bar);

		/*Indice.setFont(font);
		panneau.add(Indice);
		panneau.setBorder(new TitledBorder(BorderFactory.createDashedBorder(Color.gray),"panneau"));*/
		
		textField1.setText("Veuillez choisir votre fichier ^__^");
		textField1.setFont(font);
		textField1.setPreferredSize(new Dimension(40, 40));
		Ouvrire.setPreferredSize(new Dimension(140,40));
		Indexere.setPreferredSize(new Dimension(140,40));
		Rechercherr.setPreferredSize(new Dimension(140,40));
		list.add(textField1);
		list.add(Ouvrire);
		list.add(Indexere);
		list.add(Rechercherr);
		list.setBorder(new TitledBorder(BorderFactory.createDashedBorder(Color.gray),"list"));
		
		panneau.add( new JScrollPane(edit));
		panneau1.add(labelName);
		add(list);
		add(panneau);
		add(panneau1);

		// Chargement du fichier
		ActionListener Ouverture = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource().equals(Ouvrire) || arg0.getSource().equals(Ouvrir)){
					edit.setText("");
					edit.setBackground(Color.white);
					edit.setForeground(Color.black);
					FileSystemView vue = FileSystemView.getFileSystemView();
					File home = vue.getDefaultDirectory();
					parcourFichier = new JFileChooser(home);
					parcourFichier.setAcceptAllFileFilterUsed(false);
					try{
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						SwingUtilities.updateComponentTreeUI(parcourFichier);
					}catch(InstantiationException e){
					}catch(ClassNotFoundException e){
					}catch(UnsupportedLookAndFeelException e){
					}catch(IllegalAccessException e){}
					String extAutorise[] = {"xml", "txt"};
					FileNameExtensionFilter mesfiltres = new FileNameExtensionFilter("xml,txt", extAutorise);
					parcourFichier.addChoosableFileFilter(mesfiltres);
					int rVal = parcourFichier.showOpenDialog(parcourFichier);
					if(rVal == JFileChooser.APPROVE_OPTION){
						textField1.setText(parcourFichier.getSelectedFile().getAbsolutePath());
						//edit.setForeground(Color.white);
						 try
			                {
			                    FileReader reader = new FileReader(parcourFichier.getSelectedFile().getAbsolutePath());
			                    BufferedReader br = new BufferedReader(reader);
			                    String str;
			                    while ((str = br.readLine()) != null) 
			                    	edit.append("\n"+str);
			                    br.close();
			                    edit.requestFocus();
			                }
			                catch(Exception e2) { System.out.println(e2); }
					}
					if (rVal == JFileChooser.CANCEL_OPTION) 
						textField1.setText("vous avez annulez l'ouverture du fichier");
				}
			}
		};
	
		//Generation d'indexation de fichier
		ActionListener Indexation = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String dir = parcourFichier.getSelectedFile().getParent()+"Index";
				if(e.getSource().equals(Indexere) || e.getSource().equals(Indexer)){
					try {
						try {
				            boolean result = false;
				            File directory = new File(dir);
				            if (!directory.exists()) {
				                result = directory.mkdir();
				            }
				        }catch(Exception ea) {
				            ea.printStackTrace();
				        }
						tester.createIndex(dir,parcourFichier.getSelectedFile().getParent());
						JOptionPane.showMessageDialog(null, "Le Document est bien Indexé");
						} catch (IOException io) {io.printStackTrace();} 
				}
			}
		};
		
		//Recherche des mote cles
		ActionListener Recherche = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource().equals(Rechercherr) || e.getSource().equals(Rechercher)){
					String name = JOptionPane.showInputDialog("Le Mot clé que vous cherchez ?");
					
					try {
						String valR= testSearch.search(name,parcourFichier.getSelectedFile().getParent()+"Index",parcourFichier.getSelectedFile().getParent());
						try
		                {
		                    FileReader reader = new FileReader(valR);
		                    BufferedReader br = new BufferedReader(reader);
		                    String str;
		                    while ((str = br.readLine()) != null) {
		                    	lnr = new LineNumberReader(reader);
		                    	String[] splited = str.split("\\b+");
		                    	if (Arrays.asList(splited).contains(name)) {
		                    		edit.setForeground(Color.RED);
		                    		edit.append("\n\t"+name+"\n\t");
		                    		textField1.setText("votre Mot cherch� exite dans le fichier :"+parcourFichier.getSelectedFile().getName());
		                         }
		                    	else{
		                    		edit.append("\n"+str);
		                    	}
		                    }
		                    br.close();
		                    edit.requestFocus();
		                }
		                catch(Exception e2) { System.out.println(e2); }
						System.out.println(valR);
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				}	
			}
			
		};
		
		Ouvrir.addActionListener(Ouverture);
		Ouvrire.addActionListener(Ouverture);
		Indexere.addActionListener(Indexation);
		Indexer.addActionListener(Indexation);
		Rechercherr.addActionListener(Recherche);
		Rechercher.addActionListener(Recherche);
		
		//Les parametres primo
		setLayout(flow);
		setTitle("Indexateur");
		setSize(1220,800);
		setResizable(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new Acceuil();
	}
}
