package ca.screenshot.dashboard.entity;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author plaguemorin
 *         Date: 29/05/12
 *         Time: 10:51 AM
 */
@Entity
@XmlRootElement
public class GeneratedSource extends AbstractValueObject {
	private String remoteIdentifier;
	private String generator;

	public String getRemoteIdentifier() {
		return remoteIdentifier;
	}

	public void setRemoteIdentifier(String remoteIdentifier) {
		this.remoteIdentifier = remoteIdentifier;
	}

	public void updateWith(final GeneratedSource generatedSource) {
		super.updateWith(generatedSource);
		this.remoteIdentifier = generatedSource.remoteIdentifier;
	}


	@XmlID
	public String getGenerator() {
		return this.generator;
	}

	public void setGenerator(String generator) {
		this.generator = generator;
	}
}
