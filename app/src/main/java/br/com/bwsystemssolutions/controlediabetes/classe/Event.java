package br.com.bwsystemssolutions.controlediabetes.classe;

public class Event {
    private int id;
    private String text;
    private String source;
    private int sort;

    public static final String BUNDLE_STRING_KEY = Event.class.toString();
    public static final String SOURCE_FROM_APP = "app";
    public static final String SOURCE_FROM_USER = "user";


    public Event(){
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getSource() {
        return source;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    //Este método foi reescrito para ser utilizado em spinners sem a necessidade de criar novo adapter.
    //O spinner utilizando do método toString para exibir o texto como item.
    @Override
    public String toString() {
        return text;
    }
}
