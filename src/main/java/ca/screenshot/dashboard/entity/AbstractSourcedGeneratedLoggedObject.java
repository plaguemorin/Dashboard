package ca.screenshot.dashboard.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.unmodifiableCollection;

/**
 * @author plaguemorin
 *         Date: 29/05/12
 *         Time: 12:58 PM
 */
@MappedSuperclass
public abstract class AbstractSourcedGeneratedLoggedObject extends AbstractLoggedValueObject implements SourcedGeneratedObject {
	@OneToMany
	private List<GeneratedSource> generatorList = new ArrayList<>();

	@XmlElementWrapper(name = "generators")
	@XmlElement(name = "generator")
	public Collection<GeneratedSource> getGenerators() {
		return unmodifiableCollection(generatorList);
	}

	@Override
	public void addGenerator(GeneratedSource generatedSource) {
		this.generatorList.add(generatedSource);
	}

	@Override
	public void updateGenerators(final SourcedGeneratedObject objectSourced) {
		for (final GeneratedSource generatedSource : objectSourced.getGenerators()) {
			if (this.generatorList.contains(generatedSource)) {
				this.generatorList.get(this.generatorList.indexOf(generatedSource)).updateWith(generatedSource);
			} else {
				this.generatorList.add(generatedSource);
			}
		}
	}


	@Override
	public GeneratedSource getForSourceName(String name) {
		for (final GeneratedSource generatedSource : generatorList) {
			if (generatedSource.getGenerator().equals(name)) {
				return generatedSource;
			}
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
			gen.setGenerator(sourceName);
			this.addGenerator(gen);
		}
	}
}
