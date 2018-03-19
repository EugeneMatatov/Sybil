public class NodeList {
 	public  String valueTextField;
 	public  String lblAddParameter;
 	public  String comboBox;
 	NodeList next = null;
	
public NodeList(String param,String filter,String value){
	this.valueTextField=value;
	this.lblAddParameter= param;
	this.comboBox=filter;
	
	//this.next = next;
}
public String toString() {
    String result = lblAddParameter + " ," + comboBox + " ," + valueTextField;
    if (next != null) {
        result += next.toString();
        
    }
    return result;
}
}