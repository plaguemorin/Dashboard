package ca.screenshot.dashboard.entity;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

/**
 * @author plaguemorin
 *         Date: 29/05/12
 *         Time: 12:58 PM
 */
@MappedSuperclass
public abstract class AbstractSourcedGeneratedLoggedObject extends AbstractLoggedValueObject implements SourcedGeneratedObject {
	@ManyToMany(cascade = {CascadeType.PERSIST})
	private Map<String, GeneratedSource> generatorList = new HashMap<>();

	@XmlTransient
	public Map<String, GeneratedSource> getGenerators() {
		return unmodifiableMap(generatorList);
	}

	@Override
	public void addGenerator(final String sourceName, GeneratedSource generatedSource) {
		this.generatorList.put(sourceName, generatedSource);
	}

	@Override
	public void updateGenerators(final SourcedGeneratedObject objectSourced) {

	}

	@Override
	public GeneratedSource getForSourceName(String name) {
		if (this.generatorList.containsKey(name)) {
			return this.generatorList.get(name);
		}

		return null;
	}

	@Override
	public String getRemoteIdentifier(final String sourceName) {
		final GeneratedSource generatedSource = this.getForSourceName(sourceName);
		return (generatedSource != null) ? generatedSource.getRemoteIdentifier() : null;
	}

	@Override
	public void setRemoteIdentifier(String sourceName, String remoteIdentifier) {
		final GeneratedSource generatedSource = this.getForSourceName(sourceName);
		if (generatedSource != null) {
			generatedSource.setRemoteIdentifier(remoteIdentifier);
		} else {
			final GeneratedSource gen = new GeneratedSource();
			gen.setRemoteIdentifier(remoteIdentifier);
			gen.setLastUpdated(Calendar.getInstance().getTime());
			this.addGenerator(sourceName, gen);
		}
	}
}
