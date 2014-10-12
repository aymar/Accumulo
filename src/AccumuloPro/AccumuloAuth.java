package AccumuloPro;

//import java.util.HashMap;
import java.util.Map.Entry;
//import java.util.Map;










import javax.swing.JOptionPane;

import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Mutation;
//import org.apache.accumulo.core.data.Mutation;
import org.apache.accumulo.core.data.Range;
import org.apache.accumulo.core.data.Value;
//import org.apache.accumulo.core.iterators.user.GrepIterator;
import org.apache.accumulo.core.security.Authorizations;
import org.apache.accumulo.core.security.ColumnVisibility;
//import org.apache.accumulo.core.security.ColumnVisibility;
import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.accumulo.core.client.BatchWriter;
import org.apache.accumulo.core.client.BatchWriterConfig;
//import org.apache.accumulo.core.client.BatchWriter;
//import org.apache.accumulo.core.client.BatchWriterConfig;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.Instance;
import org.apache.accumulo.core.client.MutationsRejectedException;
import org.apache.accumulo.core.client.TableExistsException;
//import org.apache.accumulo.core.client.IteratorSetting;
//import org.apache.accumulo.core.client.MutationsRejectedException;
//import org.apache.accumulo.core.client.TableExistsException;
import org.apache.accumulo.core.client.TableNotFoundException;
import org.apache.accumulo.core.client.ZooKeeperInstance;
import org.apache.accumulo.core.client.security.tokens.PasswordToken;
import org.apache.accumulo.core.client.Scanner;
//import org.apache.hadoop.io.Text;
//import org.apache.accumulo.core.client.BatchWriter;


import org.apache.hadoop.io.Text;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

class AbstractCommand {

protected Connector connection = null;

public void setConnection(Connector connection) {
    this.connection = connection;
}
}

class CreateTableCommand extends AbstractCommand {
    //@Parameter(names = {"-t","--table"}, description = "Table name to create", required = true)
    private String table;
    //private String Description;

    public void setTable(String tableName){
    	this.table = tableName;    	
    }
    public String getTable(){
    	return this.table;
    }
    /*public void setDescription(String Des){
    	this.Description = Des;    	
    }*/
    public void run() throws AccumuloException, AccumuloSecurityException, TableExistsException {
       // System.out.println("Creating table " + table);
        if (connection.tableOperations().exists(this.table)) {
            throw new RuntimeException("Table " + this.table + " already exists");
        } else {
            connection.tableOperations().create(this.table);
            System.out.println("Table created");
        }
    }
}

class InsertRowCommand extends AbstractCommand {
    //@Parameter(names = {"-t","--table"}, description = "Table to scan", required = true)
    //private String table;

    //@Parameter(names = {"-r","--rowid"}, description = "Row Id to insert", required = true)
   // private String rowId;

    //@Parameter(names = {"-cf","--columnFamily"}, description = "Column Family to insert", required = true)
    //private String cf;

    //@Parameter(names = {"-cq","--columnQualifier"}, description = "Column Qualifier to insert", required = true)
   // private String cq;

   // @Parameter(names = {"-val","--value"}, description = "Value to insert", required = true)
    //private String val;

    //@Parameter(names = {"-ts","--timestamp"}, description = "Timestamp to use on row insert")
    //private long timestamp;

    //@Parameter(names = {"-a","--auths"}, description = "ColumnVisiblity expression to insert with data")
    //private String auths;
    
    

    public void run(String tableName,String rowId,String colFamily,String colQualifier,String auths,String val ) throws TableNotFoundException, MutationsRejectedException {
        //System.out.println("Writing mutation for " + rowId);
        BatchWriter bw = connection.createBatchWriter(tableName, new BatchWriterConfig());

        Mutation m = new Mutation(new Text(rowId));
        m.put(new Text(colFamily), new Text(colQualifier), new ColumnVisibility(auths), new Value(val.getBytes()));
        bw.addMutation(m);
        bw.close();
    }
}

class ScanCommand extends AbstractCommand {
//@Parameter(names = {"-t","--table"}, description = "Table to scan", required = true)
private String table;

//@Parameter(names = {"-r","--row"}, description = "Row to scan")
private String row = null;

//@Parameter(names = {"-a","--auths"}, description = "Comma separated list of scan authorizations")
private String auths;

private String user;

private String outPut;

private String topicText;

public void setUser(String user) {
    this.user = user;
}

public void setRow(String rowId) {
	this.setTable();
	this.row = rowId;
}

public void setColumn(String topic)
{
	this.topicText = topic;
}
public void setTable() {
	this.table = "books";
}
public void run(String Option) throws TableNotFoundException, AccumuloException, AccumuloSecurityException {
    String table = this.table;
    String row = this.row;
    //JOptionPane.showMessageDialog(null, row);
	//System.out.println("Scanning " + table);
    Authorizations authorizations = null;
    if ((null != auths) && (!auths.equals("SCAN_ALL"))) {
        //System.out.println("Using scan auths " + auths);
    	//JOptionPane.showMessageDialog(null, auths);
        authorizations = new Authorizations(auths.split(","));
    } else {
    	//JOptionPane.showMessageDialog(null, "Scanning with all user auths" + this.user);
    	//System.out.println("Scanning with all user auths");
        authorizations = connection.securityOperations().getUserAuthorizations(this.user);        
    }
    Scanner scanner = connection.createScanner(table, authorizations);
    if (null != row) {
        //System.out.println("Scanning for row " + row);
    	//scanner.setRange(new Range(row));  // row = textbook1
    	//scanner.fetchColumnFamily()
    	//JOptionPane.showMessageDialog(null, Option);
    	scanner.setRange(new Range(row));
    } else {
    	//JOptionPane.showMessageDialog(null, Option);
    	Text topicVal =  new Text(this.topicText);
		scanner.fetchColumnFamily(topicVal);	
      //  System.out.println("Scanning for all rows");
    }
    //System.out.println("Results ->");
    String result = "<html><body><table border=1 cellpadding=1 width='100%'>";    
    for (Entry<Key,Value> entry : scanner) {
    	 result += "<tr><td colspan=2 align=left><b>" + entry.getKey().getRow() + "</b></td></tr>";
    	 result += "<tr><td> Category: </td><td>" + entry.getKey().getColumnFamily() + "</td></tr>";
    	 result += "<tr><td> AccessTo: </td><td>" + entry.getKey().getColumnQualifier() + "</td></tr>";
    	 result += "<tr><td> Data: </td><td>" + entry.getValue() + "</td></tr>";
    }
    result += "</table></body></html>";
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
          //  scanCommand.run();
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

