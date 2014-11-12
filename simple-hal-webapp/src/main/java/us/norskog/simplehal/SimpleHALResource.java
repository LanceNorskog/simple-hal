/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package us.norskog.simplehal;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import us.norskog.simplehal._Embedded;
import us.norskog.simplehal._Links;
import us.norskog.simplehal.Link;
import us.norskog.simplehal.Items;

@Path("/simplehal")
public class SimpleHALResource {
	static Value value = null;

	@GET
    @Produces("text/plain")
	public Value getString() {
		if (value == null)
			value = new Value();
		return value;
	}

	@GET
	@Path("/links")
	@_Links(links = {
			@Link(rel = "self", href = "/simplehal/links", title = "Self"),
			@Link(rel = "first", href = "/simplehal/links?id=${response.first}", title = "First") })
	@Produces({ "application/hal+json", MediaType.APPLICATION_JSON })
	public Value getValueLinks() {
		if (value == null)
			value = new Value();
		return value;
	}

	@GET
	@Path("embedded")
	@_Links(links = {
			@Link(rel = "self", href = "/simplehal/embedded", title = "Self"),
			@Link(rel = "first", href = "/simplehal/embedded?id=${response.first}", title = "First") })
	@_Embedded({
			@Items(name = "Mappacious", items = "${response.map}", links = { @Link(rel = "only", href = "/simplehal/embedded?id=${item.value}", title = "id ${item.key}") }) })
	@Produces({ "application/hal+json", MediaType.APPLICATION_JSON })
	public Value getValueEmbedded() {
		if (value == null)
			value = new Value();
		return value;
	}

	@GET
	@Path("check")
	@_Links(links = {
			@Link(rel = "self", href = "/simplehal/embedded", title = "Self"),
			@Link(rel = "first", check = "${response.doFirst}", href = "/simplehal/embedded?id=${response.first}", title = "First") })
	@_Embedded({
			@Items(name = "Mappacious", items = "${response.map}", links = {
					@Link(rel = "only", check = "${item.key == 0}", href = "/simplehal/embedded?id=${item.value}", title = "id ${item.key}"),
					@Link(rel = "first", check = "${response.doFirst}", href = "/simplehal/embedded?id=${response.first}", title = "First") }) 
			})
	@Produces({ "application/hal+json", MediaType.APPLICATION_JSON })
	public Value getValueChecks() {
		if (value == null)
			value = new Value();
		return value;
	}

	static void setValue(Value newValue) {
		value = newValue;
	}
}
