package net.minexon.minexon.models;

public class SocialMedia {

    private final String instagram;
    private final String twitter;
    private final String youtube;
    private final String discord;
    private final String skype;

    public SocialMedia(String instagram, String twitter, String youtube, String discord, String skype) {
        this.instagram = instagram;
        this.twitter = twitter;
        this.youtube = youtube;
        this.discord = discord;
        this.skype = skype;
    }

    public SocialMedia() {
        this.instagram = "-";
        this.twitter = "-";
        this.youtube = "-";
        this.discord = "-";
        this.skype = "-";
    }

    public String getInstagram() {
        return instagram;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getYoutube() {
        return youtube;
    }

    public String getDiscord() {
        return discord;
    }

    public String getSkype() {
        return skype;
    }

}
