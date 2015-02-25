package twitter;

public class Tweets {
	 
    private String day;
    private String hour;
    private String user;
    private String text;
     
    public String getDay() {
        return day;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public String getHour() {
        return hour;
    }
    public void setHour(String hour) {
        this.hour = hour;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
     
   // @Override
    //public String toString(){
      //  return "ID="+id+",Name="+name+",Role="+role+",Salary="+salary+"\n";
   // }
}