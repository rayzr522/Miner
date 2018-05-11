
package com.rayzr522.miner.arena;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class Region {
	
	private int minX;
	private int minY;
	private int minZ;
	
	private int maxX;
	private int maxY;
	private int maxZ;
	
	public Region(int x1, int y1, int z1, int x2, int y2, int z2) {
		
		if (x1 <= x2) {
			
			this.minX = x1;
			this.maxX = x2;
			
		} else {
			
			this.minX = x2;
			this.maxX = x1;
			
		}
		
		if (y1 <= y2) {
			
			this.minY = y1;
			this.maxY = y2;
			
		} else {
			
			this.minY = y2;
			this.maxY = y1;
			
		}
		
		if (z1 <= z2) {
			
			this.minZ = z1;
			this.maxZ = z2;
			
		} else {
			
			this.minZ = z2;
			this.maxZ = z1;
			
		}
		
	}
	
	public Region(Location v1, Location v2) {
		
		this(v1.getBlockX(), v1.getBlockY(), v1.getBlockZ(), v2.getBlockX(), v2.getBlockY(), v2.getBlockZ());
		
	}
	
	public Region(Vector v1, Vector v2) {
		this(v1.getBlockX(), v1.getBlockY(), v1.getBlockZ(), v2.getBlockX(), v2.getBlockY(), v2.getBlockZ());
	}
	
	public Vector getMin() {
		
		return new Vector(this.minX, this.minY, this.minZ);
		
	}
	
	public Vector getMax() {
		
		return new Vector(this.maxX, this.maxY, this.maxZ);
		
	}
	
	public int getMinX() {
		return minX;
	}
	
	public void setMinX(int minX) {
		this.minX = minX;
	}
	
	public int getMinY() {
		return minY;
	}
	
	public void setMinY(int minY) {
		this.minY = minY;
	}
	
	public int getMinZ() {
		return minZ;
	}
	
	public void setMinZ(int minZ) {
		this.minZ = minZ;
	}
	
	public int getMaxX() {
		return maxX;
	}
	
	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}
	
	public int getMaxY() {
		return maxY;
	}
	
	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}
	
	public int getMaxZ() {
		return maxZ;
	}
	
	public void setMaxZ(int maxZ) {
		this.maxZ = maxZ;
	}
	
	public List<Vector> getBlocks() {
		
		List<Vector> blocks = new ArrayList<Vector>();
		
		for (int x = minX; x <= maxX; x++) {
			
			for (int y = minY; y <= maxY; y++) {
				
				for (int z = minZ; z <= maxZ; z++) {
					
					blocks.add(new Vector(x, y, z));
					
				}
				
			}
			
		}
		
		return blocks;
		
	}
	
	public boolean inside(Vector vec) {
		
		int x = vec.getBlockX();
		int y = vec.getBlockY();
		int z = vec.getBlockZ();
		
		return inside(x, y, z);
		
	}
	
	public boolean inside(Location loc) {
		
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		
		return inside(x, y, z);
		
	}
	
	public boolean inside(Block block) {
		
		Location loc = block.getLocation();
		
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		
		return inside(x, y, z);
		
	}
	
	public boolean inside(int x, int y, int z) {
		
		boolean xOk = (x >= minX && x <= maxX);
		boolean yOk = (y >= minY && y <= maxY);
		boolean zOk = (z >= minZ && z <= maxZ);
		
		boolean ok = (xOk && yOk && zOk);
		
		return ok;
		
	}
	
	public List<Location> getFramePoints(World world) {
		
		List<Location> result = new ArrayList<Location>();
		
		for (int i = minX; i <= maxX; i++) {
			result.add(world.getBlockAt(i, minY, minZ).getLocation());
			result.add(world.getBlockAt(i, minY, maxZ).getLocation());
			result.add(world.getBlockAt(i, maxY, minZ).getLocation());
			result.add(world.getBlockAt(i, maxY, maxZ).getLocation());
		}
		
		for (int j = minY; j <= maxY; j++) {
			result.add(world.getBlockAt(minX, j, minZ).getLocation());
			result.add(world.getBlockAt(minX, j, maxZ).getLocation());
			result.add(world.getBlockAt(maxX, j, minZ).getLocation());
			result.add(world.getBlockAt(maxX, j, maxZ).getLocation());
		}
		
		for (int k = minZ; k <= maxZ; k++) {
			result.add(world.getBlockAt(minX, minY, k).getLocation());
			result.add(world.getBlockAt(minX, maxY, k).getLocation());
			result.add(world.getBlockAt(maxX, minY, k).getLocation());
			result.add(world.getBlockAt(maxX, maxY, k).getLocation());
		}
		
		return result;
		
	}
	
}