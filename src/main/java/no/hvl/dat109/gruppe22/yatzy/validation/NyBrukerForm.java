package no.hvl.dat109.gruppe22.yatzy.validation;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NyBrukerForm {

    @NotBlank
    @Size(min = 3, max = 20)
    private String brukernavn;

    @NotBlank
    @Size(max = 100)
    private String navn;

    @NotBlank
    @Size(max = 100)
    @Email
    private String epost;

    @NotBlank
    @Size(min = 8)
    private String passord;

    private String trimInput(String s) {
        return (s == null) ? null : s.strip();
    }

    public String getBrukernavn() {
        return brukernavn;
    }

    public void setBrukernavn(String brukernavn) {
        this.brukernavn = trimInput(brukernavn);
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = trimInput(navn);
    }

    public String getEpost() {
        return epost;
    }

    public void setEpost(String epost) {
        this.epost = trimInput(epost);
    }

    public String getPassord() {
        return passord;
    }

    public void setPassord(String passord) {
        this.passord = trimInput(passord);
    }
}
