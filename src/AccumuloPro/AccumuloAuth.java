package AccumuloPro;

//import java.util.HashMap;
import java.util.Map.Entry;
//import java.util.Map;

import org.apache.accumulo.core.data.Key;
//import org.apache.accumulo.core.data.Mutation;
import org.apache.accumulo.core.data.Range;
import org.apache.accumulo.core.data.Value;
//import org.apache.accumulo.core.iterators.user.GrepIterator;
import org.apache.accumulo.core.security.Authorizations;
//import org.apache.accumulo.core.security.ColumnVisibility;
import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
//import org.apache.accumulo.core.client.BatchWriter;
//import org.apache.accumulo.core.client.BatchWriterConfig;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.Instance;
//import org.apache.accumulo.core.client.IteratorSetting;
//import org.apache.accumulo.core.client.MutationsRejectedException;
//import org.apache.accumulo.core.client.TableExistsException;
import org.apache.accumulo.core.client.TableNotFoundException;
import org.apache.accumulo.core.client.ZooKeeperInstance;
import org.apache.accumulo.core.client.security.tokens.PasswordToken;
import org.apache.accumulo.core.client.Scanner;
//import org.apache.hadoop.io.Text;
//import org.apache.accumulo.core.client.BatchWriter;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

class AbstractCommand {

protected Connector connection = null;

public void setConnection(Connector connection) {
    this.connection = connection;
}
}

class ScanCommand extends AbstractCommand {
//@Parameter(names = {"-t","--table"}, description = "Table to scan", required = true)
private String table;

//@Parameter(names = {"-r","--row"}, description = "Row to scan")
private String row;

//@Parameter(names = {"-a","--auths"}, description = "Comma separated list of scan authorizations")
private String auths;

private String user;

private String outPut;

public void setUser(String user) {
    this.user = user;
}

public void setRow(String rowId) {
	this.setTable();
	this.row = rowId;
}

public void setTable() {
	this.table = "books";
}
public void run() throws TableNotFoundException, AccumuloException, AccumuloSecurityException {
    String table = this.table;
    String row = this.row;
	//System.out.println("Scanning " + table);
    Authorizations authorizations = null;
    if ((null != auths) && (!auths.equals("SCAN_ALL"))) {
        //System.out.println("Using scan auths " + auths);
        authorizations = new Authorizations(auths.split(","));
    } else {
        //System.out.println("Scanning with all user auths");
        authorizations = connection.securityOperations().getUserAuthorizations(this.user);
    }
    Scanner scanner = connection.createScanner(table, authorizations);
    if ((null != row) && (!row.equals("SCAN_ALL"))) {
        //System.out.println("Scanning for row " + row);
        scanner.setRange(new Range(row));
    } else {
      //  System.out.println("Scanning for all rows");
    }
    //System.out.println("Results ->");
    String result = "";
    for (Entry<Key,Value> entry : scanner) {
         result += "  " + entry.getKey() + " " + entry.getValue() + "<br>";
    }
    this.setOutPutResult(result);
}

public void setOutPutResult(String outPut) {
	this.outPut = outPut;
}
public String getOutPutResult() {
	return this.outPut;
}
}
public class AccumuloAuth {
	//@Parameter(names = {"-p", "--password"}, description = "Accumulo user password", required = true)
private String password;

// @Parameter(names = {"-u","--user"}, description = "Accumulo user", required = true)
private String user;

//@Parameter(names = {"-i","--instance"}, description = "Accumulo instance name", required = true)
private String instance = "accumulo";

//@Parameter(names = {"-z","--zookeepers"}, description = "Comma-separated list of zookeepers", required = true)
private String zookeepers = "localhost:2181";

public  void setUser(String user)
{
	this.user = user;
}

public  void setPassword(String pwd)
{
	this.password = pwd;
}

public Connector getConnection() throws AccumuloException, AccumuloSecurityException {
    Instance i = new ZooKeeperInstance(this.instance, this.zookeepers);
    Connector conn = i.getConnector(this.user, new PasswordToken(this.password));
    return conn;
}

public static void main(String[] args) 
{
	AccumuloAuth javaExample = new AccumuloAuth();
    JCommander jc = new JCommander(javaExample);

    //CreateTableCommand createTableCommand = new CreateTableCommand();
    //jc.addCommand("create", createTableCommand);
    //InsertRowCommand insertCommand = new InsertRowCommand();
    //jc.addCommand("insert", insertCommand);
    ScanCommand scanCommand = new ScanCommand();
    jc.addCommand("scan", scanCommand);
    //GrepCommand grepCommand = new GrepCommand();
    //jc.addCommand("grep", grepCommand);

    try {
        jc.parse(args);
        String command = jc.getParsedCommand();
        if (null == command) {
            throw new RuntimeException("You didn't choose a command");
        }/* else if (command.equals("create")) {
            System.out.println("Running create command");
            createTableCommand.setConnection(javaExample.getConnection());
            createTableCommand.run();
        }*/ /*else if (command.equals("insert")) {
            System.out.println("Running insert command");
            insertCommand.setConnection(javaExample.getConnection());
            insertCommand.run();
        }*/ else if (command.equals("scan")) {
            System.out.println("Running scan command");
            scanCommand.setConnection(javaExample.getConnection());
            scanCommand.setUser(javaExample.user);
            scanCommand.run();
        } /*else if (command.equals("grep")) {
            System.out.println("Running grep command");
            grepCommand.setConnection(javaExample.getConnection());
            grepCommand.setUser(javaExample.user);
            grepCommand.run();
        }*/ else {
            throw new RuntimeException("Unrecognized command " + command);
        }
    } catch (Exception e) {
        System.err.println("Error: " +  e.getMessage());
        jc.usage();
    }
}
}

