package mestrado.ipg.condomastercrud;

public class Assembleia {


    private long _id;
    private String USER_ID;
    private String MEETING_DATE;
    private String PLACE_ID;
    private String DESCRIPTION;
    private String TITLE;
    private static Assembleia instance;


    public Assembleia(long ID, String USER_ID, String MEETING_DATE, String PLACE_ID, String DESCRIPTION, String TITLE) {
        this._id = ID;
        this.USER_ID = USER_ID;
        this.MEETING_DATE = MEETING_DATE;
        this.PLACE_ID = PLACE_ID;
        this.DESCRIPTION = DESCRIPTION;
        this.TITLE = TITLE;
    }

    public Assembleia() {
        this._id = 0;
        this.USER_ID = "";
        this.MEETING_DATE = "";
        this.PLACE_ID = "";
        this.DESCRIPTION = "";
        this.TITLE = "";
    }


    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getMEETING_DATE() {
        return MEETING_DATE;
    }

    public void setMEETING_DATE(String MEETING_DATE) {
        this.MEETING_DATE = MEETING_DATE;
    }

    public String getPLACE_ID() {
        return PLACE_ID;
    }

    public void setPLACE_ID(String PLACE_ID) {
        this.PLACE_ID = PLACE_ID;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }


}
