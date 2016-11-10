/*****************************************************************
SEE HLA Starter Kit -  A Java framework to develop HLA Federates
in the context of the SEE (Simulation Exploration Experience) 
project.
Copyright (c) 2014, SMASH Lab - University of Calabria (Italy), 
All rights reserved.

GNU Lesser General Public License (GNU LGPL).

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 3.0 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library. 
If not, see http://http://www.gnu.org/licenses/
*****************************************************************/

package model;

import skf.coder.HLAinteger32BECoder;
import skf.coder.HLAunicodeStringCoder;
import skf.model.object.annotations.Attribute;
import skf.model.object.annotations.ObjectClass;


/**
 * The Class Rover.
 */
@ObjectClass(name = "PhysicalEntity.Rover")
public class Rover {
	
	/** The rover id counter. */
	private static int roverIDCounter = 0;
	
	/** The identifier. */
	@Attribute(name = "id", coder = HLAinteger32BECoder.class)
	private Integer identifier;
	
	/** The name. */
	@Attribute(name = "name", coder = HLAunicodeStringCoder.class)
	private String name = null;
	
	/** The entity. */
	@Attribute(name = "entity_name", coder = HLAunicodeStringCoder.class)
	private String entity;
	
	
	/**
	 * Instantiates a new rover.
	 */
	public Rover(){}

	/**
	 * Instantiates a new rover.
	 * 
	 * @param name
	 *            the name
	 * @param entity
	 *            the entity
	 */
	public Rover(String name, String entity) {
		this.identifier = roverIDCounter;
		roverIDCounter++;
		
		this.name = name;
		this.entity = entity;
	}


	/**
	 * Gets the identifier.
	 * 
	 * @return the identifier
	 */
	public Integer getIdentifier() {
		return identifier;
	}


	/**
	 * Sets the identifier.
	 * 
	 * @param identifier
	 *            the new identifier
	 */
	public void setIdentifier(Integer identifier) {
		this.identifier = identifier;
	}


	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the entity.
	 * 
	 * @return the entity
	 */
	public String getEntity() {
		return entity;
	}

	/**
	 * Sets the entity.
	 * 
	 * @param entity
	 *            the new entity
	 */
	public void setEntity(String entity) {
		this.entity = entity;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Rover [identifier=" + identifier + ", name=" + name
				+ ", entity=" + entity + "]";
	}

}
