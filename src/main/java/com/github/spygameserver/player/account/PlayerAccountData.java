package com.github.spygameserver.player.account;

public class PlayerAccountData {

    private final int playerId;
    private final String email;
    private final String username;
    private AccountVerificationStatus accountVerificationStatus;

    public PlayerAccountData(int playerId, String email, String username,
                                     AccountVerificationStatus accountVerificationStatus) {
        this.playerId = playerId;
        this.email = email;
        this.username = username;
        this.accountVerificationStatus = accountVerificationStatus;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public AccountVerificationStatus getAccountVerificationStatus() {
        return accountVerificationStatus;
    }

    public void setAccountVerificationStatus(AccountVerificationStatus accountVerificationStatus) {
        this.accountVerificationStatus = accountVerificationStatus;
    }

}
