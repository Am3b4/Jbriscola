package model;

public class ProfiloUtente {
    private String nickname;
    private String percorsoAvatar;
    private int partiteGiocate, vinte, perse;

    public ProfiloUtente(String nickname, String percorsoAvatar) {
        this.nickname = nickname;
        this.percorsoAvatar = percorsoAvatar;
    }

    public String getNickname() {return nickname;}
    public String getPercorsoAvatar() {return percorsoAvatar;}
    public int getPartiteGiocate() {return partiteGiocate;}
    public int getVinte() {return vinte;}
    public int getPerse() {return perse;}

    public void setNickname(String nickname) {this.nickname = nickname;}
    public void setPercorsoAvatar(String percorsoAvatar) {this.percorsoAvatar = percorsoAvatar;}
    public void incrementaPartiteGiocate() {this.partiteGiocate++;}
    public void incrementaVinte() {this.vinte++;}
    public void incrementaPerse() {this.perse++;}
    
}
