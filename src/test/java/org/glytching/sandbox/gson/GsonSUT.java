package org.glytching.sandbox.gson;

import java.util.Objects;

public class GsonSUT {
    private String hotStreak;
    private String leagueName;
    private String tier;
    private String freshBlood;
    private String playerOrTeamId;
    private String leaguePoints;
    private String inactive;
    private String rank;
    private String veteran;
    private String queueType;
    private String losses;
    private String playerOrTeamName;
    private String wins;

    public String getHotStreak() {
        return hotStreak;
    }

    public void setHotStreak(String hotStreak) {
        this.hotStreak = hotStreak;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getFreshBlood() {
        return freshBlood;
    }

    public void setFreshBlood(String freshBlood) {
        this.freshBlood = freshBlood;
    }

    public String getPlayerOrTeamId() {
        return playerOrTeamId;
    }

    public void setPlayerOrTeamId(String playerOrTeamId) {
        this.playerOrTeamId = playerOrTeamId;
    }

    public String getLeaguePoints() {
        return leaguePoints;
    }

    public void setLeaguePoints(String leaguePoints) {
        this.leaguePoints = leaguePoints;
    }

    public String getInactive() {
        return inactive;
    }

    public void setInactive(String inactive) {
        this.inactive = inactive;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getVeteran() {
        return veteran;
    }

    public void setVeteran(String veteran) {
        this.veteran = veteran;
    }

    public String getQueueType() {
        return queueType;
    }

    public void setQueueType(String queueType) {
        this.queueType = queueType;
    }

    public String getLosses() {
        return losses;
    }

    public void setLosses(String losses) {
        this.losses = losses;
    }

    public String getPlayerOrTeamName() {
        return playerOrTeamName;
    }

    public void setPlayerOrTeamName(String playerOrTeamName) {
        this.playerOrTeamName = playerOrTeamName;
    }

    public String getWins() {
        return wins;
    }

    public void setWins(String wins) {
        this.wins = wins;
    }

    @Override
    public String toString() {
        return "ClassPojo [hotStreak = " + hotStreak + ", leagueName = " + leagueName + ", tier = " + tier + ", " +
                "freshBlood = " + freshBlood + ", playerOrTeamId = " + playerOrTeamId + ", leaguePoints = " +
                leaguePoints + ", inactive = " + inactive + ", rank = " + rank + ", veteran = " + veteran + ", " +
                "queueType = " + queueType + ", losses = " + losses + ", playerOrTeamName = " + playerOrTeamName + "," +
                " wins = " + wins + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GsonSUT gsonSUT = (GsonSUT) o;
        return Objects.equals(hotStreak, gsonSUT.hotStreak) &&
                Objects.equals(leagueName, gsonSUT.leagueName) &&
                Objects.equals(tier, gsonSUT.tier) &&
                Objects.equals(freshBlood, gsonSUT.freshBlood) &&
                Objects.equals(playerOrTeamId, gsonSUT.playerOrTeamId) &&
                Objects.equals(leaguePoints, gsonSUT.leaguePoints) &&
                Objects.equals(inactive, gsonSUT.inactive) &&
                Objects.equals(rank, gsonSUT.rank) &&
                Objects.equals(veteran, gsonSUT.veteran) &&
                Objects.equals(queueType, gsonSUT.queueType) &&
                Objects.equals(losses, gsonSUT.losses) &&
                Objects.equals(playerOrTeamName, gsonSUT.playerOrTeamName) &&
                Objects.equals(wins, gsonSUT.wins);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hotStreak, leagueName, tier, freshBlood, playerOrTeamId, leaguePoints, inactive, rank, veteran, queueType, losses, playerOrTeamName, wins);
    }
}
