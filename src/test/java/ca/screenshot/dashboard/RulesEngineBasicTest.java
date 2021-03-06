package ca.screenshot.dashboard;

import junit.framework.Assert;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.event.rule.DebugAgendaEventListener;
import org.drools.event.rule.DebugWorkingMemoryEventListener;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

public class RulesEngineBasicTest {

	private KnowledgeBase kbase;
	private KnowledgeBuilder kbuilder;
	private KnowledgeRuntimeLogger logger;
	private StatefulKnowledgeSession ksession;


	@Before
	public void setUp() {
		kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

		// this will parse and compile in one step
		kbuilder.add(ResourceFactory.newClassPathResource("src/main/resources/helloWorld.drl"), ResourceType.DRL);

		// Check the builder for errors
		if (kbuilder.hasErrors()) {
			System.out.println(kbuilder.getErrors().toString());
			throw new RuntimeException("Unable to compile \"HelloWorld.drl\".");
		}

		// get the compiled packages (which are serializable)
		final Collection<KnowledgePackage> pkgs = kbuilder.getKnowledgePackages();

		// add the packages to a knowledgebase (deploy the knowledge packages).
		kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(pkgs);

		ksession = kbase.newStatefulKnowledgeSession();

		// setup the debug listeners
		ksession.addEventListener(new DebugAgendaEventListener());
		ksession.addEventListener(new DebugWorkingMemoryEventListener());

		// setup the audit logging
		logger = KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
	}

	@Test
	public void simpleTest() {
		final Message message = new Message();
		message.setMessage("Hello World");
		message.setStatus(Message.HELLO);
		ksession.insert(message);

		ksession.fireAllRules();
		logger.close();
		ksession.dispose();

		Assert.assertEquals(Message.GOODBYE, message.getStatus());
	}

	public class Message {
		public static final int HELLO = 0;
		public static final int GOODBYE = 1;

		private String message;
		private int status;

		public Message() {

		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}
	}
}
