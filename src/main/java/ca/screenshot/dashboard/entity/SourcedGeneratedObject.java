package ca.screenshot.dashboard.entity;

import java.util.Map;

/**
 * @author plaguemorin
 *         Date: 29/05/12
 *         Time: 1:00 PM
 */
public interface SourcedGeneratedObject {
	Map<String, GeneratedSource> getGenerators();

	void addGenerator(final String sourceName, final GeneratedSource generatedSource);

	void updateGenerators(final SourcedGeneratedObject objectSourced);

	GeneratedSource getForSourceName(final String name);

	String getRemoteIdentifier(String sourceName);

	void setRemoteIdentifier(final String sourceName, String remoteIdentifier);
}
