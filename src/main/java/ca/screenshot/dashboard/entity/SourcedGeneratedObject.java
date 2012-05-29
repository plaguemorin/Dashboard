package ca.screenshot.dashboard.entity;

import java.util.Collection;

/**
 * @author plaguemorin
 *         Date: 29/05/12
 *         Time: 1:00 PM
 */
public interface SourcedGeneratedObject {
	Collection<GeneratedSource> getGenerators();

	void addGenerator(final GeneratedSource generatedSource);

	void updateGenerators(SourcedGeneratedObject objectSourced);

	GeneratedSource getForSourceName(final String name);

	String getRemoteIdentifier(String sourceName);

	void setRemoteIdentifier(String sourceName, String remoteIdentifier);
}
