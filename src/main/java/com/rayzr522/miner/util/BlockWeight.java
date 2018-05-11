
package com.rayzr522.miner.util;

import org.bukkit.Material;

public class BlockWeight {
	
	private Material block;
	private int weight;
	
	public BlockWeight(Material block, int weight) {
		
		this.block = block;
		this.weight = weight;
		
	}
	
	public Material getBlock() {
		return block;
	}
	
	public void setBlock(Material block) {
		this.block = block;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
}
