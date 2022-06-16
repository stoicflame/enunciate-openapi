package dk.jyskebank.tools.enunciate.modules.openapi.security;

import org.apache.commons.lang.StringUtils;

import com.webcohesion.enunciate.EnunciateLogger;

import dk.jyskebank.tools.enunciate.modules.freemarker.Typed1ArgTemplateMethod;
import dk.jyskebank.tools.enunciate.modules.openapi.ObjectTypeRenderer;
import dk.jyskebank.tools.enunciate.modules.openapi.yaml.IndentationPrinter;

/**
 * @author FabianHalbmann
 */
public class SecuritySchemeRenderer extends Typed1ArgTemplateMethod<String, String> {

	private final EnunciateLogger logger;
	private final ObjectTypeRenderer objectTypeRenderer;
	private final SecurityScheme securityScheme;

	public SecuritySchemeRenderer(EnunciateLogger logger, ObjectTypeRenderer objectTypeRenderer,
			SecurityScheme securityScheme) {
		super(String.class);
		this.logger = logger;
		this.objectTypeRenderer = objectTypeRenderer;
		this.securityScheme = securityScheme;
	}

	@Override
	protected String exec(String nextLineIndent) {
		IndentationPrinter ip = new IndentationPrinter(nextLineIndent, objectTypeRenderer.doRemoveObjectPrefix());
		renderLines(ip);
		return ip.toString();
	}

	private void renderLines(IndentationPrinter ip) {
		ip.add(securityScheme.getSchemeId() + ":");
		ip.nextLevel();
		ip.add("type: " + securityScheme.getType());
		if (StringUtils.isNotBlank(securityScheme.getScheme())) {
			ip.add("scheme: " + securityScheme.getScheme());
		}
		// TODO: implement rendering of "complete" scheme!
	}

}
