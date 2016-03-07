public class User {
	private String name;
	private double insulinResistance;
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
	
	public void setInsulinResistance(double insulinResistance){
		this.insulinResistance = insulinResistance;
	}
	
	public double getInsulinResistance(){
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
	
	public int calculateInsulinDose(int carboMass, int actualGlycemy, double insulinResistance){
		double ww = carboMass / 10;
		int glycemyAboveUnderNormal = actualGlycemy - 100;
		double foodDose = ww / insulinResistance;
		double correctionDose = glycemyAboveUnderNormal / insulinResistance;
		int insulinDose = (int) (foodDose + correctionDose);
		
		return insulinDose;
	}
	
	public int calculateInsulinDose(int actualGlycemy, int insulinResistance){
		int glycemyAboveUnderNormal = actualGlycemy - 100;
		int correctionDose = glycemyAboveUnderNormal / insulinResistance;
		
		return correctionDose;
	}
	
	public void  setGlycemyArray(int arraySize){
		this.glycemyArray = new String[arraySize+1];//dodatkowa kom�rka do zapisu obliczonej warto�ci
				
	}
	
	public void putValues(int counter, String value){//dodawanie elementu. value wartosc komorki
			this.glycemyArray[counter] = value;//bez p�tli bo by�a p�tla w petli i nie dzia�a�o
	}
	public String getLastValue(){
		int lastIndex = this.glycemyArray.length -1;
		String lastValue = this.glycemyArray[lastIndex];
		return lastValue;
	}

}
