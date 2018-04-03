package perceptron8;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.UniqueKey;
import org.jooq.impl.DefaultDSLContext;
import org.junit.Test;

public class MetaTest {
	private static final String URL = "jdbc:firebirdsql://localhost/test.fdb?encoding=NONE";
	private static final String USERNAME = "sysdba";
	private static final String PASSWORD = "masterkey";

	@Test
	public void test() throws Exception {
		try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
			try (DSLContext context = new DefaultDSLContext(connection, SQLDialect.FIREBIRD_3_0)) {
				List<Schema> schemas = context.meta().getSchemas();
				assertTrue(schemas.size() == 1);
				Schema onlySchema = schemas.iterator().next();
				Table<?> parents = onlySchema.getTable("PARENTS");
				assertTrue(parents != null);
				UniqueKey<?> primaryKey = parents.getPrimaryKey();
				assertTrue(primaryKey != null);
				// FIXME NPE below
				List<?> references = primaryKey.getReferences(); 
				assertTrue(references.size() == 1);
			}
		}
	}
}
