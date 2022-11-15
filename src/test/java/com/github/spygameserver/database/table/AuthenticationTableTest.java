package com.github.spygameserver.database.table;

import com.github.glusk.caesar.Hex;
import com.github.glusk.srp6_variables.SRP6CustomIntegerVariable;
import com.github.spygameserver.DatabaseRequiredTest;
import com.github.spygameserver.auth.PlayerAuthenticationData;
import com.github.spygameserver.database.ConnectionHandler;
import com.github.spygameserver.database.DatabaseCreator;
import com.github.spygameserver.database.impl.AuthenticationDatabase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.nio.ByteOrder;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthenticationTableTest implements DatabaseRequiredTest {

    private static final int TEST_PLAYER_ID = 101;

    private AuthenticationTable authenticationTable;
    private ConnectionHandler connectionHandler;

    @BeforeAll
    public void setupAuthenticationTable() {
        File file = getValidCredentialsFile();

        DatabaseCreator<AuthenticationDatabase> databaseCreator = new DatabaseCreator<>(file, "auth_db", true);
        AuthenticationDatabase authenticationDatabase = databaseCreator.createDatabaseFromFile(AuthenticationDatabase::new);

        authenticationTable = authenticationDatabase.getAuthenticationTable();
        connectionHandler = authenticationDatabase.getNewConnectionHandler(false);

        // Clear the database test table so that we can test without any interfering records
        authenticationTable.dropTableSecure(connectionHandler);
        authenticationTable.initialize(connectionHandler);
    }

    @Test
    public void testAllPaths() {
        createAndVerifyAuthenticationData();
        updateAndVerifyAuthenticationData();
    }

    private void createAndVerifyAuthenticationData() {
        PlayerAuthenticationData addedPlayerAuthenticationData = new PlayerAuthenticationData(TEST_PLAYER_ID,
                getExampleSalt('A'), getExampleVerifier('B'));
        authenticationTable.addPlayerAuthenticationRecord(connectionHandler, addedPlayerAuthenticationData);


        PlayerAuthenticationData retrievedPlayerAuthenticationData = authenticationTable
                .getPlayerAuthenticationRecord(connectionHandler, TEST_PLAYER_ID);

        Assertions.assertEquals(addedPlayerAuthenticationData, retrievedPlayerAuthenticationData,
                "Data inserted into the database does not match data retrieved from the database.");
    }

    private void updateAndVerifyAuthenticationData() {
        PlayerAuthenticationData updatedPlayerAuthenticationData = new PlayerAuthenticationData(TEST_PLAYER_ID,
                getExampleSalt('C'), getExampleVerifier('D'));
        authenticationTable.updatePlayerAuthenticationRecord(connectionHandler, updatedPlayerAuthenticationData);

        PlayerAuthenticationData retrievedPlayerAuthenticationData = authenticationTable
                .getPlayerAuthenticationRecord(connectionHandler, TEST_PLAYER_ID);

        Assertions.assertEquals(updatedPlayerAuthenticationData, retrievedPlayerAuthenticationData,
                "Data updated in the database does not match data retrieved from the database.");
    }

    @AfterAll
    @Override
    public void closeOpenConnections() {
        closeOpenConnections(connectionHandler);
    }

    private SRP6CustomIntegerVariable getExampleSalt(char c) {
        return repeatCharacter(c, 32);
    }

    private SRP6CustomIntegerVariable getExampleVerifier(char c) {
        return repeatCharacter(c, 256);
    }

    private SRP6CustomIntegerVariable repeatCharacter(char c, int numTimes) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 1; i <= numTimes; i++) {
            stringBuilder.append(c);
        }

        String bytesStringForm = stringBuilder.toString();
        return new SRP6CustomIntegerVariable(new Hex(bytesStringForm), ByteOrder.BIG_ENDIAN);
    }

}
