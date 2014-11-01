package AccumuloPro;

import java.util.Map.Entry;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Mutation;
import org.apache.accumulo.core.data.Range;
import org.apache.accumulo.core.data.Value;
import org.apache.accumulo.core.security.Authorizations;
import org.apache.accumulo.core.security.ColumnVisibility;
import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.accumulo.core.client.BatchWriter;
import org.apache.accumulo.core.client.BatchWriterConfig;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.Instance;
import org.apache.accumulo.core.client.MutationsRejectedException;
import org.apache.accumulo.core.client.TableExistsException;
import org.apache.accumulo.core.client.TableNotFoundException;
import org.apache.accumulo.core.client.ZooKeeperInstance;
import org.apache.accumulo.core.client.security.tokens.PasswordToken;
import org.apache.accumulo.core.client.Scanner;
import org.apache.hadoop.io.Text;
import com.beust.jcommander.JCommander;

class AbstractCommand {
	protected Connector connection = null;
	public void setConnection(Connector connection) {
	    this.connection = connection;
	}
}

class CreateTableCommand extends AbstractCommand {
    //@Parameter(names = {"-t","--table"}, description = "Table name to create", required = true)
    private String table;
    public void setTable(String tableName){
    	this.table = tableName;    	
    }

    public String getTable(){
    	return this.table;
    }

    public void run() throws AccumuloException, AccumuloSecurityException, TableExistsException {
        if (connection.tableOperations().exists(this.table)) {
            throw new RuntimeException("Table " + this.table + " already exists");
        } else {
            connection.tableOperations().create(this.table);
        }
    }
}

class InsertRowCommand extends AbstractCommand {
    public void run(String tableName,String rowId,String colFamily,String colQualifier,String auths,String val ) throws TableNotFoundException, MutationsRejectedException {
        BatchWriter bw = connection.createBatchWriter(tableName, new BatchWriterConfig());

        Mutation m = new Mutation(new Text(rowId));
        m.put(new Text(colFamily), new Text(colQualifier), new ColumnVisibility(auths), new Value(val.getBytes()));
        bw.addMutation(m);
        bw.close();
    }
}

class ScanCommand extends AbstractCommand {
	//(names = {"-t","--table"}, description = "Table to scan", required = true)
	private String table;
	
	//(names = {"-r","--row"}, description = "Row to scan")
	private String row = null;
	
	//(names = {"-a","--auths"}, description = "Comma separated list of scan authorizations")
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
	    Authorizations authorizations = null;
	    if ((null != auths) && (!auths.equals("SCAN_ALL"))) {
	        authorizations = new Authorizations(auths.split(","));
	    } else {
	    	authorizations = connection.securityOperations().getUserAuthorizations(this.user);        
	    }
	    Scanner scanner = connection.createScanner(table, authorizations);
	    if (null != row) {
	        scanner.setRange(new Range(row));
	    } else {
	    	Text topicVal =  new Text(this.topicText);
			scanner.fetchColumnFamily(topicVal);	
	    }
	    String result = "<html><body><table border=1 cellpadding=1 width='100%'>";    
	    for (Entry<Key,Value> entry : scanner) {
	    	 if (Option == "topic") {
	    		 result += "<tr><td colspan=2 align=left><b>" + entry.getKey().getColumnFamily() + "</b></td></tr>";
	    		 result += "<tr><td> Text Book: </td><td>" + entry.getKey().getRow() + "</td></tr>";
	    	 } else {
	    		 result += "<tr><td colspan=2 align=left><b>" + entry.getKey().getRow() + "</b></td></tr>";
	    		 result += "<tr><td> Category: </td><td>" + entry.getKey().getColumnFamily() + "</td></tr>";
	    	 }    	 
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

	private String password;
	private String user;
	private String instance = "accumulo";
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
	    ScanCommand scanCommand = new ScanCommand();
	    jc.addCommand("scan", scanCommand);
	    try {
	        jc.parse(args);
	        String command = jc.getParsedCommand();
	        if (null == command) {
	            throw new RuntimeException("You didn't choose a command");
	        } else if (command.equals("scan")) {
	            System.out.println("Running scan command");
	            scanCommand.setConnection(javaExample.getConnection());
	            scanCommand.setUser(javaExample.user);
	        } else {
	            throw new RuntimeException("Unrecognized command " + command);
	        }
	    } catch (Exception e) {
	        System.err.println("Error: " +  e.getMessage());
	        jc.usage();
	    }
	}
}