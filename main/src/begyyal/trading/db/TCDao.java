package begyyal.trading.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import begyyal.commons.object.collection.XList.XListGen;
import begyyal.trading.db.object.TCTable;

public class TCDao {

    private final Path path;

    private TCDao(Path path) {
	this.path = path;
    }

    public static TCDao newi() throws IOException {
	var pathStr = ResourceBundle.getBundle("common").getString("tablePath");
	var path = Paths.get(pathStr);
	if (!Files.exists(path)) {
	    Path pp = path;
	    var ppl = XListGen.<Path>newi();
	    while (!Files.exists(pp = pp.getParent()))
		ppl.add(pp);
	    for (var rpp : ppl.reverse())
		Files.createDirectory(rpp);
	    Files.createFile(path);
	}
	return new TCDao(path);
    }

    public TCTable read() throws IOException {
	var lines = Files.readAllLines(this.path);
	return TCTable.of(lines);
    }

    public void write(TCTable table) throws IOException {
	Files.write(this.path, table.serialize());
    }
}
