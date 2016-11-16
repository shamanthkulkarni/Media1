package methode.methodeelectronics_newdb;


public class PHOTO {
    String filename;
    public PHOTO(String filename)
    {
        this.filename = filename;
    }
    public PHOTO()
    {

    }
    public String getname()
    {
        return this.filename;
    }
    public void setFilename(String filename)
    {
        this.filename = filename;
    }

}