import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridLayout;
import java.awt.CardLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import jxl.Workbook;
import jxl.biff.formula.ParseContext;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.awt.event.ActionEvent;
import org.apache.poi.common.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.xy.XYSeries;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import javax.swing.JTextPane;
import java.awt.SystemColor;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Component;

import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.Button;
import javax.swing.BoxLayout;
import java.awt.Font;
import java.awt.Toolkit;

public class UserInterface {

	private JFrame frmKalkulatorDiabetyka;
	private JTextField tfLogin;
	private JTextField tfPassword;
	private JTextField tfNewUserName;
	private JTextField tfNewUserInsulinResistance;
	private JTextField tfNewUserPassword;
	private JTextField tfNewUserWeight;
	private JPanel panelLogIn;
	private JPanel panelNewUser;
	private JButton btnAdUser;
	private JButton btnCreateNewUser;
	private JTextPane textPane;
	private JTextField tFNewUserInfo;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserInterface window = new UserInterface();
					window.frmKalkulatorDiabetyka.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	File userData = new File("usersData.xls");//tworze plik z danymi uzytkownika
	File foodData = new File ("foodData.xls");//tworze plik z baza danych ¿ywnosci	
	String sheetName; //aktywny po zalogowaniu arkusz
	User acctualUser = new User();
	int lastRow;
	int counter=0;//licznik rekordow w bazie
	int acctSheetIndex;
	String[][] dataUsers;
	String[] columnsUsers = {"Data","Czas","Glikemia","Bolus"};
	String[][] dataFood;
	String[] columnsFood = {"Produkt","Energia [kcal]","Bia³ko [g]","T³uszcz [g]","Wêglowodany [g]"};
	DefaultTableModel dtModelHistory = new DefaultTableModel(dataUsers, columnsUsers);
	DefaultTableModel dtModelFood = new DefaultTableModel(dataFood, columnsFood);
	DefaultTableModel dtModelMeal = new DefaultTableModel(dataFood, columnsFood);
	Object[] tempRow;
	Object[] tempRowFood;
	SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.YYYY");
    double carboQuantity=0;//po wciscnieciu reset ma sie zerowaæ
    Object[] mealRow;
	
	
	private JTextField tfLoginInfo;
	private JTabbedPane tabbedPanelMainWindow;
	private JPanel tabbedPaneCalculator;
	private JTextField tfActGlycemy;
	private JTextField tfCalculatedInsulinDose;
	private JTable table;
	private JTable tableMeal;
	private JPanel panelHistory;
	private JTable tableUserData;
	private JPanel panelFood;
	private JTable tableFood;
	private JScrollPane scrollPane;
	private Button btnAddFood;
	private JLabel lblMealCorrection;
	private JTextField tfMealCorrection;
	private JLabel lblPosiek;

	/**
	 * Create the application.
	 */
	public UserInterface() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmKalkulatorDiabetyka = new JFrame();
		frmKalkulatorDiabetyka.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Figo\\Desktop\\aplikacja Kalkulator diabetyka ver 0.01\\ikona.png"));
		frmKalkulatorDiabetyka.setTitle("Kalkulator Diabetyka");
		frmKalkulatorDiabetyka.setBounds(100, 100, 580, 500);
		frmKalkulatorDiabetyka.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmKalkulatorDiabetyka.getContentPane().setLayout(new CardLayout(0, 0));
		
		panelLogIn = new JPanel();
		frmKalkulatorDiabetyka.getContentPane().add(panelLogIn, "name_693674465952");
		panelLogIn.setLayout(null);
		panelLogIn.setVisible(true);
		
		tfLogin = new JTextField();
		tfLogin.setBounds(195, 123, 163, 20);
		panelLogIn.add(tfLogin);
		tfLogin.setColumns(10);
		
		tfPassword = new JTextField();
		tfPassword.setBounds(195, 179, 163, 20);
		panelLogIn.add(tfPassword);
		tfPassword.setColumns(10);
		
		JLabel lblLogin = new JLabel("Login:");
		lblLogin.setBounds(249, 98, 46, 14);
		panelLogIn.add(lblLogin);
		
		JLabel lblPassword = new JLabel("Haslo:");
		lblPassword.setBounds(249, 154, 46, 14);
		panelLogIn.add(lblPassword);
		
		JButton btnLogin = new JButton("Zaloguj");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {//modul logowania
				//wczytujê baze danych zywnosci do programu
				if(foodData.isFile()){
					try {
						HSSFWorkbook workbookFood = new HSSFWorkbook(new FileInputStream(foodData));
						HSSFSheet sheetFood = workbookFood.getSheet("table");
						int numOfFoodRows = sheetFood.getPhysicalNumberOfRows();
						int firstRow =sheetFood.getFirstRowNum()+1;
						//int cellquantity;
						for (int i=firstRow; i<numOfFoodRows-1;i++){
							
							HSSFRow rowToPut = sheetFood.getRow(i);
							tempRowFood =new Object[] {rowToPut.getCell(0),rowToPut.getCell(1),rowToPut.getCell(2),rowToPut.getCell(3),rowToPut.getCell(4)};
							dtModelFood.addRow(tempRowFood);
							//cellquantity =rowToPut.getLastCellNum() - rowToPut.getFirstCellNum();
							//System.out.println(cellquantity);
						}
						//System.out.println("liczba wierszy w zywnosci: "+numOfFoodRows);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				//HSSFWorkbook workbook;
				if (!userData.isFile()){
					tfLoginInfo.setText("Podaj dane logowania,\n lub stwórz U¿ytkownika");
				}else{
				try {
					HSSFWorkbook workbookUsers = new HSSFWorkbook(new FileInputStream(userData));
					int numOfSheets = workbookUsers.getNumberOfSheets();
					
					String password;
					for ( int i=0; i<numOfSheets; i++){
						//pobieram login i haslo
						sheetName = workbookUsers.getSheetName(i);
						HSSFSheet sheet = workbookUsers.getSheetAt(i);
						HSSFRow row = sheet.getRow(1);
						HSSFCell cell = row.getCell(1);
						password = cell.getStringCellValue();
					if ((sheetName.equals(tfLogin.getText()))&&(password.equals(tfPassword.getText()))){
						acctSheetIndex=i;//pobieram nr aktualnie u¿ywanego arkusza
						tabbedPanelMainWindow.setVisible(true);
						panelLogIn.setVisible(false);
						//uzupe³niam wczesniej stworzony obiekt acctUser danymi z arkusza
						lastRow = sheet.getLastRowNum();
						int filledRowsNum = lastRow - 5;//bo zaczynam uzupelnianie bazy od 5 wiersza
						acctualUser.setName(sheetName);
						acctualUser.setGlycemyArray(filledRowsNum);//pobrac ilosc rekordow z pliku
						//pobieram insulinoopornosc
						cell = row.getCell(2);
						double iResDouble=cell.getNumericCellValue();
						int iResInt =(int) iResDouble;
						acctualUser.setInsulinResistance(iResInt);
						//tworzenie wykresu 
						
						//TimeSeries glycemyChart = new TimeSeries("Glycemy",Day.class);
												
						for (int j=6;j<lastRow+1;j++,counter++){
							HSSFRow rowToPut = sheet.getRow(j);
							HSSFCell cellToPut = rowToPut.getCell(2);
							acctualUser.putValues(counter,cellToPut.getStringCellValue());//dzia³a dodawanie rekordow z bazy do obiektu
							//dodawanie rekordow z bazy do tabeli
							tempRow =new Object[] {rowToPut.getCell(0),rowToPut.getCell(1),rowToPut.getCell(2),rowToPut.getCell(3)};
							dtModelHistory.addRow(tempRow);
							//glycemyChart.add(new Day(rowToPut.getCell(0)),rowToPut.getCell(2));
						}
						/*for(int k=0;k<acctualUser.glycemyArray.length-1;k++){//sprawdzam czy dziala uzupelnianie obiektu
							System.out.println(acctualUser.glycemyArray[k]);
						}*/
						//System.out.println(acctualUser.glycemyArray[0]);
						//System.out.println(lastRow);
						System.out.println("dlugosc macierzy:"+ acctualUser.glycemyArray.length);
						
					}else tfLoginInfo.setText("Niepoprawne has³o, lub nazwa u¿ytkownika");
					workbookUsers.close();
				}
					}
				 catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
			}
			
		});
		btnLogin.setBounds(226, 210, 100, 43);
		panelLogIn.add(btnLogin);
		
		btnAdUser = new JButton("Dodaj Osobe");
		btnAdUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panelNewUser.setVisible(true);
				panelLogIn.setVisible(false);
			}
		});
		btnAdUser.setBounds(205, 264, 144, 23);
		panelLogIn.add(btnAdUser);
		
		tfLoginInfo = new JTextField();
		tfLoginInfo.setHorizontalAlignment(SwingConstants.LEFT);
		tfLoginInfo.setEditable(false);
		tfLoginInfo.setBounds(10, 431, 544, 20);
		panelLogIn.add(tfLoginInfo);
		tfLoginInfo.setColumns(10);
		
		panelNewUser = new JPanel();
		frmKalkulatorDiabetyka.getContentPane().add(panelNewUser, "name_700146164437");
		panelNewUser.setLayout(null);
		panelNewUser.setVisible(false);
		
		tfNewUserName = new JTextField();
		tfNewUserName.setBounds(120, 8, 214, 20);
		panelNewUser.add(tfNewUserName);
		tfNewUserName.setColumns(10);
		
		tfNewUserInsulinResistance = new JTextField();
		tfNewUserInsulinResistance.setBounds(120, 39, 214, 20);
		panelNewUser.add(tfNewUserInsulinResistance);
		tfNewUserInsulinResistance.setColumns(10);
		
		tfNewUserPassword = new JTextField();
		tfNewUserPassword.setBounds(120, 101, 214, 20);
		panelNewUser.add(tfNewUserPassword);
		tfNewUserPassword.setColumns(10);
		
		tfNewUserWeight = new JTextField();
		tfNewUserWeight.setBounds(120, 70, 214, 20);
		panelNewUser.add(tfNewUserWeight);
		tfNewUserWeight.setColumns(10);
		
		JLabel lblName = new JLabel("Nazwa:");
		lblName.setBounds(10, 11, 46, 14);
		panelNewUser.add(lblName);
		
		JLabel lblInsulinResistance = new JLabel("Insulinoopornosc:");
		lblInsulinResistance.setBounds(10, 42, 100, 14);
		panelNewUser.add(lblInsulinResistance);
		
		JLabel lblNewPassword = new JLabel("Haslo:");
		lblNewPassword.setBounds(10, 104, 46, 14);
		panelNewUser.add(lblNewPassword);
		
		JLabel lblWeight = new JLabel("Waga:");
		lblWeight.setBounds(10, 73, 46, 14);
		panelNewUser.add(lblWeight);
		
		tFNewUserInfo = new JTextField();
		tFNewUserInfo.setEditable(false);
		tFNewUserInfo.setBounds(10, 231, 414, 20);
		panelNewUser.add(tFNewUserInfo);
		tFNewUserInfo.setColumns(10);
		
		btnCreateNewUser = new JButton("Stworz");
		btnCreateNewUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//tworzenie uzytkownika i zapis do bazy danych//  Dodac obsluge bledow danych
				
				User newUser = new User();
				try{//wyjatek wprowadzenia niepoprawnych danych do dopracowania
				newUser.setName(tfNewUserName.getText());
				newUser.setInsulinResistance(Integer.parseInt(tfNewUserInsulinResistance.getText()));
				newUser.setWeight(Integer.parseInt(tfNewUserWeight.getText()));
				newUser.setPassword(tfNewUserPassword.getText());
				}catch(NumberFormatException n){
					tFNewUserInfo.setText("WprowadŸ poprawne dane!");
				}
												
				//sprawdzam czy jest plik z danymi uzytkownikow jesli nie to go tworze
								
				if (userData.isFile()){
					try {//otwieram plik do odczytu
						HSSFWorkbook workbookUsers = new HSSFWorkbook(new FileInputStream(userData));
						//HSSFSheet sheet = workbook.getSheet(newUser.getName());
						int numOfSheets = workbookUsers.getNumberOfSheets();
						//tFNewUserInfo.setText(workbook.getSheetName(0));
						int sheetInd = 0;
						for ( int i=0; i<numOfSheets; i++,sheetInd++){//sprawdzamy czy uzytkownik o takiej nazwie istnieje
							if (new String(workbookUsers.getSheetName(i)).equals(newUser.getName())){//porownanie dwoch stringow
								tFNewUserInfo.setText("Uzytkownik o tej nazwie istnieje. WprowadŸ inna nazwê");	}}							
							//}else {tFNewUserInfo.setText("chujnia");
								HSSFSheet newSheet = workbookUsers.createSheet();//nowy sheet
								workbookUsers.setSheetName(sheetInd, newUser.getName());
								HSSFRow row1 = newSheet.createRow(0);
								HSSFRow row2 = newSheet.createRow(1);
								HSSFRow row3 = newSheet.createRow(5);
								row1.createCell(0).setCellValue("User Name:");
								row1.createCell(1).setCellValue("Password:");
								row1.createCell(2).setCellValue("Insulin Resistance:");
								row1.createCell(3).setCellValue("Weight:");
								
								row2.createCell(0).setCellValue(newUser.getName());
								row2.createCell(1).setCellValue(newUser.getPassword());
								row2.createCell(2).setCellValue(newUser.getInsulinResistance());
								row2.createCell(3).setCellValue(newUser.getWeight());
								
								newSheet.autoSizeColumn(0);
								newSheet.autoSizeColumn(1);
								newSheet.autoSizeColumn(2);
								newSheet.autoSizeColumn(3);
								
								row3.createCell(0).setCellValue("Data:");
								row3.createCell(1).setCellValue("Godzina:");
								row3.createCell(2).setCellValue("Glikemia:");
								row3.createCell(3).setCellValue("Dawka Insuliny:");
								
								try {
									workbookUsers.write(new FileOutputStream(userData));
								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								workbookUsers.close();
													
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				else{
				//tworzymy skoroszyt i arkusze, ale tylko jesli jeszcze nie istniej¹
				HSSFWorkbook workbook = new HSSFWorkbook();
				HSSFSheet sheet = workbook.createSheet(newUser.getName());
				HSSFRow row1 = sheet.createRow(0);
				HSSFRow row2 = sheet.createRow(1);
				HSSFRow row3 = sheet.createRow(5);
				//HSSFCell cell; //= row.createCell(0);
				row1.createCell(0).setCellValue("User Name:");
				//CellStyle colorStyle = workbook.createCellStyle();
				//colorStyle.setFillForegroundColor(new HSSFColor.LIGHT_BLUE().getIndex());
				//row1.createCell(0).setCellStyle(colorStyle);
				row1.createCell(1).setCellValue("Password:");
				row1.createCell(2).setCellValue("Insulin Resistance:");
				row1.createCell(3).setCellValue("Weight:");
				row2.createCell(0).setCellValue(newUser.getName());
				row2.createCell(1).setCellValue(newUser.getPassword());
				row2.createCell(2).setCellValue(newUser.getInsulinResistance());
				row2.createCell(3).setCellValue(newUser.getWeight());
				sheet.autoSizeColumn(0);
				sheet.autoSizeColumn(1);
				sheet.autoSizeColumn(2);
				sheet.autoSizeColumn(3);
				//tworze opisy do kolumn bazy
				row3.createCell(0).setCellValue("Date:");
				row3.createCell(1).setCellValue("Time:");
				row3.createCell(2).setCellValue("Glycemy:");
				row3.createCell(3).setCellValue("Bolus:");
				
				try {
					workbook.write(new FileOutputStream(userData));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					workbook.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				}
				//zmiana okna na to do logowania
				panelNewUser.setVisible(false);
				panelLogIn.setVisible(true);
				tfLoginInfo.setText("U¿ytkownik stworzony. Zaloguj siê.");
								
			}
				
		});
		btnCreateNewUser.setBounds(146, 157, 157, 55);
		panelNewUser.add(btnCreateNewUser);
		
		textPane = new JTextPane();
		textPane.setBounds(405, 223, -361, 28);
		panelNewUser.add(textPane);
		
		tabbedPanelMainWindow = new JTabbedPane(JTabbedPane.TOP);
		frmKalkulatorDiabetyka.getContentPane().add(tabbedPanelMainWindow, "name_2048122853700");
		
		tabbedPaneCalculator = new JPanel();
		tabbedPanelMainWindow.addTab("Kalkulator", null, tabbedPaneCalculator, null);
		tabbedPaneCalculator.setLayout(new GridLayout(9, 2, 1, 2));
		//tabbedPaneCalculator.setLayout(new BoxLayout(tabbedPaneCalculator, BoxLayout.Y_AXIS));
		
		JLabel lblActGlycemy = new JLabel("Aktualna glikemia:");
		lblActGlycemy.setFont(new Font("Tahoma", Font.PLAIN, 20));
		tabbedPaneCalculator.add(lblActGlycemy);
		
		tfActGlycemy = new JTextField();
		tfActGlycemy.setFont(new Font("Tahoma", Font.PLAIN, 20));
		tabbedPaneCalculator.add(tfActGlycemy);
		tfActGlycemy.setColumns(10);
		
		lblMealCorrection = new JLabel("Korekta na posi\u0142ek:");
		lblMealCorrection.setFont(new Font("Tahoma", Font.PLAIN, 20));
		tabbedPaneCalculator.add(lblMealCorrection);
		
		tfMealCorrection = new JTextField();
		tfMealCorrection.setFont(new Font("Tahoma", Font.PLAIN, 20));
		tfMealCorrection.setEditable(false);
		tabbedPaneCalculator.add(tfMealCorrection);
		tfMealCorrection.setColumns(10);
		
		JLabel lblCalculatedDose = new JLabel("Obliczona dawka insuliny:");
		lblCalculatedDose.setFont(new Font("Tahoma", Font.PLAIN, 20));
		tabbedPaneCalculator.add(lblCalculatedDose);
		
		tfCalculatedInsulinDose = new JTextField();
		tfCalculatedInsulinDose.setFont(new Font("Tahoma", Font.PLAIN, 20));
		tfCalculatedInsulinDose.setEditable(false);
		tabbedPaneCalculator.add(tfCalculatedInsulinDose);
		tfCalculatedInsulinDose.setColumns(10);
		
		JButton btnCalculateInsulinDose = new JButton("Oblicz dawk\u0119");
		btnCalculateInsulinDose.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnCalculateInsulinDose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//oblicznie dawki insuliny
				//acctualUser.glycemyArray[counter+1]=tfActGlycemy.getText();
				acctualUser.putValues(counter,tfActGlycemy.getText());
				acctualUser.setGlycemy(Integer.parseInt(tfActGlycemy.getText()));
				//ustawic pole insulin resisstance
				int insRes = acctualUser.getInsulinResistance();
				String dose=null;
				/*if(carboQuantity==0){
					dose =Integer.toString(acctualUser.calculateInsulinDose(acctualUser.getGlycemy(), insRes));//poprawiæ zeby actuser mialo wartosc insulinoopornosci
					tfCalcilatedInsulinDose.setText(dose);
				}
				else*/ if(carboQuantity!=0){
					dose =Integer.toString(acctualUser.calculateInsulinDose(carboQuantity,acctualUser.getGlycemy(), insRes));//poprawiæ zeby actuser mialo wartosc insulinoopornosci
					tfCalculatedInsulinDose.setText(dose);
					System.out.println("dziala");
				}else{
				dose =Integer.toString(acctualUser.calculateInsulinDose(acctualUser.getGlycemy(), insRes));//poprawiæ zeby actuser mialo wartosc insulinoopornosci
				tfCalculatedInsulinDose.setText(dose);
				}
				
				LocalDate acctDate = LocalDate.now();
				 Calendar cal = Calendar.getInstance();
			     
			     String time = timeFormat.format(cal.getTime());
			     String date = dateFormat.format(cal.getTime());
			    
				//counter++;
				try {
					HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(userData));//zapisujemy do arkusza glikemie i bolus
					HSSFSheet sheet = workbook.getSheetAt(acctSheetIndex);
					HSSFRow acctRow = sheet.createRow(lastRow+1);
					acctRow.createCell(2).setCellValue(acctualUser.getLastValue());
					acctRow.createCell(3).setCellValue(dose);		
					acctRow.createCell(0).setCellValue(date);
					acctRow.createCell(1).setCellValue(time);
					lastRow = sheet.getLastRowNum();//aktualizacja danych w jtable po kliknieciu na oblicz
					HSSFRow lastRowPut = sheet.getRow(lastRow);
					tempRow =new Object[] {lastRowPut.getCell(0),lastRowPut.getCell(1),lastRowPut.getCell(2),lastRowPut.getCell(3)};
					dtModelHistory.addRow(tempRow);
															
					try {
						workbook.write(new FileOutputStream(userData));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						workbook.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/*for(int k=0;k<acctualUser.glycemyArray.length-1;k++){//sprawdzam czy dziala uzupelnianie obiektu
					System.out.println(acctualUser.glycemyArray[k]);
				}*/
			}
		});
		tabbedPaneCalculator.add(btnCalculateInsulinDose);
		
		lblPosiek = new JLabel("Posi\u0142ek:");
		lblPosiek.setFont(new Font("Tahoma", Font.PLAIN, 20));
		tabbedPaneCalculator.add(lblPosiek);
		
		tableMeal = new JTable(dtModelMeal);
		//JScrollPane jspMeal = new JScrollPane();
		tabbedPaneCalculator.add(tableMeal);
		tabbedPaneCalculator.add(new JScrollPane(tableMeal));
		//tableMeal.add(jspMeal);
		
		panelFood = new JPanel();
		panelFood.setToolTipText("");
		tabbedPanelMainWindow.addTab("\u017Bywno\u015B\u0107", null, panelFood, null);
		panelFood.setLayout(new BorderLayout(0, 0));
		
		tableFood = new JTable(dtModelFood);
		panelFood.add(tableFood, BorderLayout.CENTER);
		tableFood.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//blokujemy przycisk jezeli zaden element nie jest wybrany
		ListSelectionModel listSelectionModel = tableFood.getSelectionModel();
		listSelectionModel.addListSelectionListener(new ListSelectionListener() {
		        public void valueChanged(ListSelectionEvent arg0) { 
		            ListSelectionModel lsm = (ListSelectionModel)arg0.getSource();
		            btnAddFood.setEnabled(!lsm.isSelectionEmpty());
		        }
		});
					
		btnAddFood = new Button("Dodaj do posi\u0142ku");
		btnAddFood.disable();		
		btnAddFood.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//double carboQuantity=0;	//przenieœæ na pocz¹tek	
				// okienko dialogowe do pobierania informacji o ilosci sk³adnika z bazy
				Component frame = null;
				//String howMuch="0";
				//Object options[]={"OK","Anuluj"};
				String howMuch = (String)JOptionPane.showInputDialog(
				                    frame,
				                    "Wpisz iloœæ bior¹c pod uwagê jednostki \n"
				                    + "podane w bazie posi³ków ³y¿ki, plastry itp.\n"
				                    +"Pamiêtaj mo¿esz podaæ tylko liczby (ca³kowite lub u³amki)",
				                    "Iloœæ sk³adnika",
				                    JOptionPane.QUESTION_MESSAGE
				                   );
				//if(howMuch==null){setLabel("adas");}
				
				double quantity = Double.parseDouble(howMuch);
				int foodTableRow = tableFood.getSelectedRow();
				//String cq =(String) tableFood.getValueAt(foodTableRow,4);
				double mealCarboQuantity = Double.parseDouble(tableFood.getValueAt(foodTableRow,4).toString())*quantity;
				//Double cq = Double.parseDouble(mealCarboQuantity);
				
				carboQuantity = carboQuantity + mealCarboQuantity;
				System.out.println(carboQuantity);
				//System.out.println(acctualUser.calculateInsulinDose(carboQuantity, acctualUser.getGlycemy(), acctualUser.getInsulinResistance()));
				//System.out.println(tableFood.getValueAt(foodTableRow,4));
				//dodac oblicznie bolusa na podstawie danych z tabeli
			    mealRow = new Object[] {tableFood.getValueAt(foodTableRow,0),tableFood.getValueAt(foodTableRow,1),
			    		tableFood.getValueAt(foodTableRow,2),tableFood.getValueAt(foodTableRow,3),tableFood.getValueAt(foodTableRow,4)};
				dtModelMeal.addRow(mealRow);
								
			}
		});
		btnAddFood.setActionCommand("Dodaj do posi\u0142ku");
		panelFood.add(btnAddFood, BorderLayout.SOUTH);
		
		//scrollPane = new JScrollPane();
		panelFood.add(new JScrollPane(tableFood));
		
		panelHistory = new JPanel();		
		tabbedPanelMainWindow.addTab("Historia", null, panelHistory, null);
		panelHistory.setLayout(new BorderLayout(0, 0));
		
		tableUserData = new JTable(dtModelHistory);
		tableUserData.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		panelHistory.add(tableUserData);
		JScrollPane jspHistory= new JScrollPane(tableUserData);
		panelHistory.add(jspHistory);
		tableUserData.setFillsViewportHeight(true);
				
		
	}
}
