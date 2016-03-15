public class User {
	private String name;
	private int insulinResistance;
	private double weight;
	private int actGlycemy;
	String [] glycemyArray;
	private String [] date;
	private String [] time;
	//private int insulinDose;
	private String password;
	
	public void setName(String name){
		//name = this.name;
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setInsulinResistance(int insulinResistance){
		this.insulinResistance = insulinResistance;
	}
	
	public int getInsulinResistance(){
		return insulinResistance;
	}
	
	public void setWeight(double weight){
		this.weight = weight;
	}
	
	public double getWeight(){
		return weight;
	}
	
	public void setGlycemy(int glycemy){
		this.actGlycemy = glycemy;
	}
	
	public int getGlycemy(){
		return actGlycemy;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public String getPassword(){
		return password;
	}
	
	public int calculateInsulinDose(double carboMass, int actualGlycemy, int insulinResistance){
		//double ww = carboMass / 10;
		int glycemyAboveUnderNormal = actualGlycemy - 100;
		int foodDose =(int)( carboMass /(double) insulinResistance);
		int correctionDose = glycemyAboveUnderNormal / insulinResistance;
		int insulinDose = (int) (foodDose + correctionDose);
		
		return insulinDose;
	}
	
	public int calculateInsulinDose(int actualGlycemy, int insulinResistance){
		int glycemyAboveUnderNormal = actualGlycemy - 100;
		int correctionDose = glycemyAboveUnderNormal / insulinResistance;
		
		return correctionDose;
	}
	
	public void  setGlycemyArray(int arraySize){
		this.glycemyArray = new String[arraySize+1];//dodatkowa komórka do zapisu obliczonej wartoœci
				
	}
	
	public void putValues(int counter, String value){//dodawanie elementu. value wartosc komorki
			this.glycemyArray[counter] = value;//bez pêtli bo by³a pêtla w petli i nie dzia³a³o
	}
	public String getLastValue(){
		int lastIndex = this.glycemyArray.length -1;
		String lastValue = this.glycemyArray[lastIndex];
		return lastValue;
	}

}
