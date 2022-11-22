package com.github.spygameserver.auth.website;

import com.github.spygameserver.database.ConnectionHandler;
import com.github.spygameserver.database.impl.GameDatabase;
import com.github.spygameserver.database.table.PlayerAccountTable;
import org.json.JSONObject;
import spark.Response;

import java.util.Map;

public class AddVerifiedEmailRoute extends VerificationRoute {

    private final GameDatabase gameDatabase;

    public AddVerifiedEmailRoute(GameDatabase gameDatabase) {
        this.gameDatabase = gameDatabase;
    }

    @Override
    public JSONObject handleAdditional(JSONObject jsonObject, Response response) {
        String email = getEmail(jsonObject);

        ConnectionHandler connectionHandler = gameDatabase.getNewConnectionHandler(false);
        PlayerAccountTable playerAccountTable = gameDatabase.getPlayerAccountTable();

        if (playerAccountTable.doesEmailAlreadyExist(connectionHandler, email)) {
            return getErrorObject("This email is already registered to another account, please sign in.");
        }

        playerAccountTable.addVerifiedEmail(connectionHandler, email);
        connectionHandler.closeAbsolutely();

        return getSuccessObject();
    }

}
