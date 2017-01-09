/**
 * Reference Frame model.
 *
 * Provides the basic data representations for a reference frame.
 *
 * @author   Edwin Z. Crues <edwin.z.crues@nasa.gov>
 * @version  0.0
 */
/**
 * Copyright 2015 Edwin Z. Crues
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package see.smackdown.reference_frame;

import see.smackdown.utilities.*;

public class ReferenceFrame {

	// Public class data.
	public String ref_frame_name;
	public String parent_frame_name;
	public double[] position = new double[3];
	public double[] velocity = new double[3];
	public Quaternion attitude = new Quaternion();
	public double[][] T_parent_body = new double[3][3];
	public double[] attitude_rate = new double[3];
	public double time;

	public ReferenceFrame(String name) {
		ref_frame_name = name;
		parent_frame_name = null;
		Vector3.initialize(position);
		Vector3.initialize(velocity);
		attitude.make_identity();
		Matrix3x3.identity(T_parent_body);
		Vector3.initialize(attitude_rate);
		time = 0.0;
	}

	public ReferenceFrame(String parent_name, String name) {
		ref_frame_name = name;
		parent_frame_name = parent_name;
		Vector3.initialize(position);
		Vector3.initialize(velocity);
		attitude.make_identity();
		Matrix3x3.identity(T_parent_body);
		Vector3.initialize(attitude_rate);
		time = 0.0;
	}

	public ReferenceFrame(ReferenceFrame orig) {
		this.ref_frame_name = orig.ref_frame_name;
		this.parent_frame_name = orig.parent_frame_name;
		Vector3.copy(orig.position, this.position);
		Vector3.copy(orig.velocity, this.velocity);
		this.attitude = orig.attitude;
		Matrix3x3.copy(orig.T_parent_body, this.T_parent_body);
		Vector3.copy(orig.attitude_rate, this.attitude_rate);
		this.time = orig.time;
	}

	public void print() {

		System.out.print(this.ref_frame_name);
		System.out.println(":");
		System.out.print("   parent frame: ");
		if (this.parent_frame_name == null) {
			System.out.println("<None>");
		} else {
			System.out.println(this.parent_frame_name);
		}
		System.out.print("   position: ");
		Vector3.print(this.position);
		System.out.print("   velocity: ");
		Vector3.print(this.velocity);
		System.out.print("   attitude: ");
		this.attitude.print();
		System.out.print("   att rate: ");
		Vector3.print(this.attitude_rate);
		return;
	}

	public void print_posvel() {

		System.out.print(this.ref_frame_name);
		System.out.println(":");
		System.out.print("   position: ");
		Vector3.print(this.position);
		System.out.print("   velocity: ");
		Vector3.print(this.velocity);
		return;
	}

	public void print_attitude() {

		System.out.print(this.ref_frame_name);
		System.out.println(":");
		System.out.print("   attitude: ");
		this.attitude.print();
		System.out.print("   att rate: ");
		Vector3.print(this.attitude_rate);
		return;
	}

}
