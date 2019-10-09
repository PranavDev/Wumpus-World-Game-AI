import java.io.*;
import java.util.*;

import com.sun.xml.internal.ws.api.pipe.NextAction;

public class WumpusWorld
{	
	
	public static class Sensors
	{
		String Type = "";
		boolean Safe = false;
		boolean Unsafe = false;
		boolean Visited = false;
		boolean Breeze = false;
		boolean Stench = false;
		boolean Glitter = false;
		boolean Bump = false;
		boolean Scream = false;
		
		Sensors(String Type, boolean Safe, boolean Unsafe, boolean Visited, boolean Breeze, boolean Stench, boolean Glitter, boolean Bump, boolean Scream)
		{
			this.Type = Type;
			this.Safe = Safe;
			this.Unsafe = Unsafe;
			this.Visited = Visited;
			this.Breeze = Breeze;
			this.Stench = Stench;
			this.Glitter = Glitter;
			this.Bump = Bump;
			this.Scream = Scream;
		}
	}
	
	
	
	public static class Score
	{
		int Gold = 0;
		int Killed = 0;
		int AnyAction = 0;
		int ArrowUsed = 0;
		
		Score(int Gold, int Killed, int AnyAction, int ArrowUsed)
		{
			this.Gold = Gold;
			this.Killed = Killed;
			this.AnyAction += AnyAction;
			this.ArrowUsed += ArrowUsed;
		}
	}


	
	public static int I = 3, J = 0, IDX = 0;
	public static int Memory[] = new int[100];
	public static int Traversed_Path[] = new int[100];
	public static int Pits[] = new int[100];
	public static int Wumpus[] = new int[100];
	public static int P_indx = 0, W_indx = 0, indx = 0, ptr = 0;
	public static int pit_pos, Wumpus_Pos, Gold_Pos;
	public static Random R = new Random(4);
	
	
	
	public static void Init_Memory_Array()
	{
		for(int i=0;i<10;i++)
		{
			Memory[i] = -1;
		}
	}
	
	
	
	//Initialize the Wumpus World Game Array
	public static void Initialize_Wumpus_World_Game_Array(Sensors Wumpus_World[][])
	{
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<4;j++)
			{
				Wumpus_World[i][j] = new Sensors("", false, false, false, false, false, false, false, false);
			}
		}
	}
	
	
	
	
	//Displays the Tile Count
	public static void Display_Wumpus_World_Tile_Count()
	{
		int count = 0;
		for(int i=0;i<4;i++)
		{
			System.out.println("\n-----------------------------------------------------------------");
			System.out.print("|\t");
			for(int j=0;j<4;j++)
			{
				System.out.print((count++)+"\t|\t");
			}
		}
		System.out.println("\n-----------------------------------------------------------------");
	}
	


	
	
	//Displays the Wumpus World Environment
	public static void Display_Wumpus_World_Game_Array(Sensors Wumpus_World[][])
	{
		for(int i=0;i<4;i++)
		{
			System.out.println("\n-----------------------------------------------------------------");
			System.out.print("|\t");
			for(int j=0;j<4;j++)
			{
				System.out.print(Wumpus_World[i][j].Type+"\t|\t");
			}
		}
		System.out.println("\n-----------------------------------------------------------------");
	}
	
	
	
	
	
	
	public static void Display_Sensors_in_Every_Tile(Sensors Wumpus_World[][])
	{
		int count = 0;
		System.out.println("\n\n\n-------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("|\tTile\t|\tSafe\t|\tUnsafe\t|\tVisited\t|\tBreeze\t|\tStench\t|\tGlitter\t|\tBump\t|\tScream\t|");
		for(int i=0;i<4;i++)
		{
			System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------");
			for(int j=0;j<4;j++)
			{
				System.out.println("|\t"+(count++)+"\t|\t"+Wumpus_World[i][j].Safe+"\t|\t"+Wumpus_World[i][j].Unsafe+"\t|\t"+Wumpus_World[i][j].Visited+"\t|\t"+Wumpus_World[i][j].Breeze+"\t|\t"+Wumpus_World[i][j].Stench+"\t|\t"+Wumpus_World[i][j].Glitter+"\t|\t"+Wumpus_World[i][j].Bump+"\t|\t"+Wumpus_World[i][j].Scream+"\t|\t");
			}
		}
		System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------");
	}
	
	
	
	
	
	
	public static void Set_Sensors_in_Adjacent_Cells(char Type, int i, int j, Sensors Wumpus_World[][])
	{
		if(Type=='P')
		{
			if(i==0 && j==0)
			{
				Wumpus_World[0][1].Breeze = true;
				Wumpus_World[0][0].Bump = true;
				Wumpus_World[0][0].Safe = false;
				Wumpus_World[0][0].Unsafe = true;
				Wumpus_World[1][0].Breeze = true;
				Wumpus_World[1][0].Bump = true;
				Wumpus_World[0][1].Type = "Breeze";
				Wumpus_World[1][0].Type = "Breeze";
			}
			else if(i==0 && j==3)
			{
				Wumpus_World[0][2].Breeze = true;
				Wumpus_World[0][3].Bump = true;
				Wumpus_World[0][3].Safe = false;
				Wumpus_World[0][3].Unsafe = true;
				Wumpus_World[1][3].Breeze = true;
				Wumpus_World[1][3].Bump = true;
				Wumpus_World[0][2].Type = "Breeze";
				Wumpus_World[1][3].Type = "Breeze";
			}
			else if(i==3 && j==0)
			{
				Wumpus_World[3][1].Breeze = true;
				Wumpus_World[3][0].Bump = true;
				Wumpus_World[3][0].Safe = false;
				Wumpus_World[3][0].Unsafe = true;
				Wumpus_World[2][0].Breeze = true;
				Wumpus_World[2][0].Bump = true;
				Wumpus_World[3][1].Type = "Breeze";
				Wumpus_World[2][0].Type = "Breeze";
			}
			else if(i==3 && j==3)
			{
				Wumpus_World[3][2].Breeze = true;
				Wumpus_World[3][3].Bump = true;
				Wumpus_World[3][3].Safe = false;
				Wumpus_World[3][3].Unsafe = true;
				Wumpus_World[2][3].Breeze = true;
				Wumpus_World[2][3].Bump = true;
				Wumpus_World[3][2].Type = "Breeze";
				Wumpus_World[2][3].Type = "Breeze";
			}
			else if((i==0 && j==1) || (i==0 && j==2))
			{
				Wumpus_World[i][j-1].Breeze = true;
				Wumpus_World[i][j+1].Breeze = true;
				Wumpus_World[i+1][j].Breeze = true;
				Wumpus_World[i][j].Safe = false;
				Wumpus_World[i][j].Unsafe = true;
				Wumpus_World[i][j-1].Type = "Breeze";
				Wumpus_World[i][j+1].Type = "Breeze";
				Wumpus_World[i+1][j].Type = "Breeze";
			}
			else if((i==3 && j==1) || (i==3 && j==2))
			{
				Wumpus_World[i][j-1].Breeze = true;
				Wumpus_World[i][j+1].Breeze = true;
				Wumpus_World[i-1][j].Breeze = true;
				Wumpus_World[i][j].Safe = false;
				Wumpus_World[i][j].Unsafe = true;
				Wumpus_World[i][j-1].Type = "Breeze";
				Wumpus_World[i][j+1].Type = "Breeze";
				Wumpus_World[i-1][j].Type = "Breeze";
			}
			else if((i==1 && j==0) || (i==2 && j==0))
			{
				Wumpus_World[i-1][j].Breeze = true;
				Wumpus_World[i+1][j].Breeze = true;
				Wumpus_World[i][j+1].Breeze = true;
				Wumpus_World[i][j].Safe = false;
				Wumpus_World[i][j].Unsafe = true;
				Wumpus_World[i-1][j].Type = "Breeze";
				Wumpus_World[i][j+1].Type = "Breeze";
				Wumpus_World[i+1][j].Type = "Breeze";
			}
			else if((i==1 && j==3) || (i==2 && j==3))
			{
				Wumpus_World[i-1][j].Breeze = true;
				Wumpus_World[i+1][j].Breeze = true;
				Wumpus_World[i][j-1].Breeze = true;
				Wumpus_World[i][j].Safe = false;
				Wumpus_World[i][j].Unsafe = true;
				Wumpus_World[i-1][j].Type = "Breeze";
				Wumpus_World[i+1][j].Type = "Breeze";
				Wumpus_World[i][j-1].Type = "Breeze";
			}
			else
			{
				Wumpus_World[i-1][j].Breeze = true;
				Wumpus_World[i+1][j].Breeze = true;
				Wumpus_World[i][j+1].Breeze = true;
				Wumpus_World[i][j-1].Breeze = true;
				Wumpus_World[i][j].Safe = false;
				Wumpus_World[i][j].Unsafe = true;
				Wumpus_World[i-1][j].Type = "Breeze";
				Wumpus_World[i+1][j].Type = "Breeze";
				Wumpus_World[i][j+1].Type = "Breeze";
				Wumpus_World[i][j-1].Type = "Breeze";
			}
		}
		else if(Type=='W')
		{
			if(i==0 && j==0)
			{
				Wumpus_World[0][1].Stench = true;
				Wumpus_World[1][0].Stench = true;
				Wumpus_World[0][0].Bump = true;
				Wumpus_World[1][0].Bump = true;
				Wumpus_World[0][0].Safe = false;
				Wumpus_World[0][0].Unsafe = true;
				Wumpus_World[0][1].Type = "Stench";
				Wumpus_World[1][0].Type = "Stench";
			}
			else if(i==0 && j==3)
			{
				Wumpus_World[0][2].Stench = true;
				Wumpus_World[0][3].Bump = true;
				Wumpus_World[1][3].Stench = true;
				Wumpus_World[0][3].Safe = false;
				Wumpus_World[0][3].Unsafe = true;
				Wumpus_World[1][3].Bump = true;
				Wumpus_World[0][2].Type = "Stench";
				Wumpus_World[1][3].Type = "Stench";
			}
			else if(i==3 && j==0)
			{
				Wumpus_World[3][1].Stench = true;
				Wumpus_World[3][0].Bump = true;
				Wumpus_World[2][0].Stench = true;
				Wumpus_World[3][0].Safe = false;
				Wumpus_World[3][0].Unsafe = true;
				Wumpus_World[2][0].Bump = true;
				Wumpus_World[3][1].Type = "Stench";
				Wumpus_World[2][0].Type = "Stench";
			}
			else if(i==3 && j==3)
			{
				Wumpus_World[3][2].Stench = true;
				Wumpus_World[3][3].Bump = true;
				Wumpus_World[2][3].Stench = true;
				Wumpus_World[3][3].Safe = false;
				Wumpus_World[3][3].Unsafe = true;
				Wumpus_World[2][3].Bump = true;
				Wumpus_World[3][2].Type = "Stench";
				Wumpus_World[2][3].Type = "Stench";
			}
			else if((i==0 && j==1) || (i==0 && j==2))
			{
				Wumpus_World[i][j-1].Stench = true;
				Wumpus_World[i][j+1].Stench = true;
				Wumpus_World[i+1][j].Stench = true;
				Wumpus_World[i][j].Safe = false;
				Wumpus_World[i][j].Unsafe = true;
				Wumpus_World[i][j-1].Type = "Stench";
				Wumpus_World[i][j+1].Type = "Stench";
				Wumpus_World[i+1][j].Type = "Stench";
			}
			else if((i==3 && j==1) || (i==3 && j==2))
			{
				Wumpus_World[i][j-1].Stench = true;
				Wumpus_World[i][j+1].Stench = true;
				Wumpus_World[i-1][j].Stench = true;
				Wumpus_World[i][j].Safe = false;
				Wumpus_World[i][j].Unsafe = true;
				Wumpus_World[i][j-1].Type = "Stench";
				Wumpus_World[i][j+1].Type = "Stench";
				Wumpus_World[i-1][j].Type = "Stench";
			}
			else if((i==1 && j==0) || (i==2 && j==0))
			{
				Wumpus_World[i-1][j].Stench = true;
				Wumpus_World[i+1][j].Stench = true;
				Wumpus_World[i][j+1].Stench = true;
				Wumpus_World[i][j].Safe = false;
				Wumpus_World[i][j].Unsafe = true;
				Wumpus_World[i-1][j].Type = "Stench";
				Wumpus_World[i][j+1].Type = "Stench";
				Wumpus_World[i+1][j].Type = "Stench";
			}
			else if((i==1 && j==3) || (i==2 && j==3))
			{
				Wumpus_World[i-1][j].Stench = true;
				Wumpus_World[i+1][j].Stench = true;
				Wumpus_World[i][j-1].Stench = true;
				Wumpus_World[i][j].Safe = false;
				Wumpus_World[i][j].Unsafe = true;
				Wumpus_World[i-1][j].Type = "Stench";
				Wumpus_World[i+1][j].Type = "Stench";
				Wumpus_World[i][j-1].Type = "Stench";
			}
			else
			{
				Wumpus_World[i-1][j].Stench = true;
				Wumpus_World[i+1][j].Stench = true;
				Wumpus_World[i][j+1].Stench = true;
				Wumpus_World[i][j-1].Stench = true;
				Wumpus_World[i][j].Safe = false;
				Wumpus_World[i][j].Unsafe = true;
				Wumpus_World[i-1][j].Type = "Stench";
				Wumpus_World[i+1][j].Type = "Stench";
				Wumpus_World[i][j+1].Type = "Stench";
				Wumpus_World[i][j-1].Type = "Stench";
			}
		}
		else if(Type=='G')
		{
			if(i==0 && j==0)
			{
				Wumpus_World[0][0].Glitter = true;
				Wumpus_World[0][0].Bump = true;
				Wumpus_World[0][0].Type = "Glitter";
			}
			else if(i==0 && j==3)
			{
				Wumpus_World[0][3].Glitter = true;
				Wumpus_World[0][3].Bump = true;
				Wumpus_World[0][3].Type = "Glitter";
			}
			else if(i==3 && j==0)
			{
				Wumpus_World[3][0].Glitter = true;
				Wumpus_World[3][0].Bump = true;
				Wumpus_World[3][0].Type = "Glitter";
			}
			else if(i==3 && j==3)
			{
				Wumpus_World[3][3].Glitter = true;
				Wumpus_World[3][3].Bump = true;
				Wumpus_World[3][3].Type = "Glitter";
			}
			else
			{
				Wumpus_World[i][j].Glitter = true;
				Wumpus_World[i][j].Type = "Glitter";
			}
		}
	}
	
	
	
	
	
	public static void Insert_into_Wumpus_World(char Type, int Pos, Sensors Wumpus_World[][])
	{
		if(Type=='P')
		{
			int indx_i = Pos/4;
			int indx_j = Pos - (4*indx_i);
			Wumpus_World[indx_i][indx_j].Type = "P";
			Set_Sensors_in_Adjacent_Cells(Type, indx_i, indx_j, Wumpus_World);
		}
		else if(Type=='W')
		{
			int indx_i = Pos/4;
			int indx_j = Pos - (4*indx_i);
			Wumpus_World[indx_i][indx_j].Type = "W";
			Set_Sensors_in_Adjacent_Cells(Type, indx_i, indx_j, Wumpus_World);
		}
		else
		{
			int indx_i = Pos/4;
			int indx_j = Pos - (4*indx_i);
			Wumpus_World[indx_i][indx_j].Type = "G";
			Set_Sensors_in_Adjacent_Cells(Type, indx_i, indx_j, Wumpus_World);
		}
	}
	
	
	
	
	
	public static void Store_Pits(int i, int j, Sensors Wumpus_World[][])
	{
		P_indx = 0;
		int a = i;
		int b = j+1;
		if((a>=0 && a<4)&&(b>=0 && b<4))
		{
			if(!Wumpus_World[a][b].Visited==true && !Wumpus_World[a][b].Safe==true)
			{
				if(Wumpus_World[a][b].Stench==true)
				{
					W_indx = 0;
					Wumpus[W_indx] = a*4 + b;
					W_indx++;
				}
				Pits[P_indx] = a*4 + b;
				P_indx++;
			}
		}
		a = i;
		b = j-1;
		if((a>=0 && a<4)&&(b>=0 && b<4))
		{
			if(!Wumpus_World[a][b].Visited==true && !Wumpus_World[a][b].Safe==true)
			{
				if(Wumpus_World[a][b].Stench==true)
				{
					W_indx = 0;
					Wumpus[W_indx] = a*4 + b;
					W_indx++;
				}
				Pits[P_indx] = a*4 + b;
				P_indx++;
			}
		}
		a = i+1;
		b = j;
		if((a>=0 && a<4)&&(b>=0 && b<4))
		{
			if(!Wumpus_World[a][b].Visited==true && !Wumpus_World[a][b].Safe==true)
			{
				if(Wumpus_World[a][b].Stench==true)
				{
					W_indx = 0;
					Wumpus[W_indx] = a*4 + b;
					W_indx++;
				}
				Pits[P_indx] = a*4 + b;
				P_indx++;
			}
		}
		a = i-1;
		b = j;
		if((a>=0 && a<4)&&(b>=0 && b<4))
		{
			if(!Wumpus_World[a][b].Visited==true && !Wumpus_World[a][b].Safe==true)
			{
				if(Wumpus_World[a][b].Stench==true)
				{
					W_indx = 0;
					Wumpus[W_indx] = a*4 + b;
					W_indx++;
				}
				Pits[P_indx] = a*4 + b;
				P_indx++;
			}
		}
	}
	
	
	
	
	
	
	public static void Store_WumpusPositions(int i, int j, Sensors Wumpus_World[][])
	{
		W_indx = 0;
		int a = i;
		int b = j+1;
		if((a>=0 && a<4)&&(b>=0 && b<4))
		{
			if(!Wumpus_World[a][b].Visited==true && !Wumpus_World[a][b].Safe==true)
			{
				if(Wumpus_World[a][b].Breeze==true)
				{
					P_indx = 0;
					Pits[P_indx] = a*4 + b;
					P_indx++;
				}
				Wumpus[W_indx] = a*4 + b;
				W_indx++;
			}
		}
		a = i;
		b = j-1;
		if((a>=0 && a<4)&&(b>=0 && b<4))
		{
			if(!Wumpus_World[a][b].Visited==true && !Wumpus_World[a][b].Safe==true)
			{
				if(Wumpus_World[a][b].Breeze==true)
				{
					P_indx = 0;
					Pits[P_indx] = a*4 + b;
					P_indx++;
				}
				Wumpus[W_indx] = a*4 + b;
				W_indx++;
			}
		}
		a = i+1;
		b = j;
		if((a>=0 && a<4)&&(b>=0 && b<4))
		{
			if(!Wumpus_World[a][b].Visited==true && !Wumpus_World[a][b].Safe==true)
			{
				if(Wumpus_World[a][b].Breeze==true)
				{
					P_indx = 0;
					Pits[P_indx] = a*4 + b;
					P_indx++;
				}
				Wumpus[W_indx] = a*4 + b;
				W_indx++;
			}
		}
		a = i-1;
		b = j;
		if((a>=0 && a<4)&&(b>=0 && b<4))
		{
			if(!Wumpus_World[a][b].Visited==true && !Wumpus_World[a][b].Safe==true)
			{
				if(Wumpus_World[a][b].Breeze==true)
				{
					P_indx = 0;
					Pits[P_indx] = a*4 + b;
					P_indx++;
				}
				Wumpus[W_indx] = a*4 + b;
				W_indx++;
			}
		}
	}
	
	
	
	
	
	
	public static void Check_Next_Move_Validity(int i, int j, Sensors Wumpus_World[][])
	{
		if((i>=0 && i<4)&&(j>=0 && j<4))
		{	
			if(Wumpus_World[i][j].Breeze==true && Wumpus_World[i][j].Stench==true && Wumpus_World[i][j].Safe==true)
			{
				if(Wumpus_World[i][j].Visited==false)
				{
					System.out.println("\n>Agent Checks Position : "+((i*4)+j));
					System.out.println("# Storing Pit Locations. ");
					Store_Pits(i,j,Wumpus_World);
					System.out.println("\n>Agent Checks Position : "+((i*4)+j));
					System.out.println("# Storing Wumpus Locations. ");
					Store_WumpusPositions(i,j,Wumpus_World);
					return;
				}
			}
			
			if(Wumpus_World[i][j].Breeze==true && Wumpus_World[i][j].Safe==true)
			{
				if(Wumpus_World[i][j].Visited==false)
				{
					System.out.println("\n>Agent Checks Position : "+((i*4)+j));
					System.out.println("# Storing Pit Locations. ");
					Store_Pits(i,j,Wumpus_World);
					return;
				}
			}
			
			if(Wumpus_World[i][j].Stench==true && Wumpus_World[i][j].Safe==true)
			{
				if(Wumpus_World[i][j].Visited==false)
				{
					System.out.println("\n>Agent Checks Position : "+((i*4)+j));
					System.out.println("# Storing Wumpus Locations. ");
					Store_WumpusPositions(i,j,Wumpus_World);
					return;
				}
			}
			
			if(Wumpus_World[i][j].Safe==true && Wumpus_World[i][j].Visited==true)
			{
				System.out.println("\n>Agent Checks Position : "+((i*4)+j));
				System.out.println("# Position Safe and Visited\n");
				return;
			}
			if(Wumpus_World[i][j].Safe==false && Wumpus_World[i][j].Visited==false)
			{
				System.out.println("\n>Agent Checks Position : "+((i*4)+j));
				System.out.println("# Position Safe - No PIT - No WUMPUS !!\n");
				Wumpus_World[i][j].Safe = true;
				Memory[indx] = (i*4)+j;
				indx++;
				return;
			}
		}
		else
		{
			System.out.println("\n>Agent Checks Position : "+((i*4)+j));
			System.out.println("# Agent Gets a BUMP\n");
			return;
		}
	}
	
	
	
	
	
	
	public static int Get_Next_Move()
	{
		int p = 99;
		for(int a=0;a<P_indx;a++)
		{
			for(int b=0;b<W_indx;b++)
			{
				if(Wumpus[b]==Pits[a])
				{
					p = Wumpus[b];
					break;
				}
			}
		}
		return p;
	}
	
	
	
	
	
	
	
	public static int DoMovement(int a,int b, Sensors Wumpus_World[][])
	{
		int pos = 0;
		int flag = 0;
		int x = a;
		int y = b+1;
		if(Wumpus_World[x][y].Safe==false && Wumpus_World[x][y].Visited==false)
		{
			pos = x*4 + y;
			flag = 1;
		}
		x = a+1;
		y = b;
		if(flag==0 && Wumpus_World[x][y].Safe==false && Wumpus_World[x][y].Visited==false)
		{
			pos = x*4 + y;
			flag = 1;
		}
		x = a-1;
		y = b;
		if(flag==0 && Wumpus_World[x][y].Safe==false && Wumpus_World[x][y].Visited==false)
		{
			pos = x*4 + y;
		}
		x = a;
		y = b-1;
		if(flag==0 && Wumpus_World[x][y].Safe==false && Wumpus_World[x][y].Visited==false)
		{
			pos = x*4 + y;
		}
		return pos;
	}
	
	
	
	
	
	public static int Play_Wumpus_World_Game(Sensors Wumpus_World[][])
	{
		int S = 0, Pos, P, Flag = 0;
		Score score = new Score(0, 0, 0, 0);
	    
	    while(Flag==0)
	    {
	    	//When we Find Gold
	    	if(Wumpus_World[I][J].Safe==true && Wumpus_World[I][J].Glitter==true)
	    	{
	    		System.out.println("\n\n-------> Agent is Located in Cell : "+((I*4)+J)+" <-------");
	    		Traversed_Path[IDX++] = (I*4)+J;
	    		if(Wumpus_World[I][J].Glitter==true)
				{
					Wumpus_World[I][J].Visited = true;
					Pos = (I*4)+J;
					System.out.println("# GOLD FOUND AT LOCATION : "+Pos+"\n");
					Flag = 1;
					score.Gold = 1000;
					score.AnyAction += -1;
					S = score.AnyAction + score.Gold + score.ArrowUsed + score.Killed;
				}
	    	}
	    	
	    	else if(((I*4)+J)==Wumpus_Pos)
	    	{
	    		System.out.println("\n\n!!! AGENT EATEN BY WUMPUS !!!\n\n");
	    		score.Killed = -1000;
	    		S = score.AnyAction + score.Gold + score.ArrowUsed + score.Killed;
	    		break;
	    	}
	    	
	    	else if(((I*4)+J)==pit_pos)
	    	{
	    		System.out.println("\n\n!!! AGENT FALLS INTO A PIT AND DIES !!!\n\n");
	    		score.Killed = -1000;
	    		S = score.AnyAction + score.Gold + score.ArrowUsed + score.Killed;
	    		break;
	    	}
	    	
	    	else if(Wumpus_World[I][J].Safe==true && Wumpus_World[I][J].Breeze==false && Wumpus_World[I][J].Stench==false)
			{
	    		if(Wumpus_World[I][J].Visited==true)
	    		{
	    			System.out.println("\n\n-------> Agent is Located in Cell : "+((I*4)+J)+" <-------");
	    			Traversed_Path[IDX++] = (I*4)+J;
	    			indx -= 1;
					P = Memory[indx];
					Memory[indx] = -1;
					I = P/4;
					J = P - (4*I);
					Wumpus_World[I][J].Safe = true;
					score.AnyAction += -1;
	    		}
	    		else
	    		{
	    			System.out.println("\n\n-------> Agent is Located in Cell : "+((I*4)+J)+" <-------");
		    		Traversed_Path[IDX++] = (I*4)+J;
					System.out.println("# Position Safe - No Breeze - No Stench");
					Check_Next_Move_Validity(I, J+1, Wumpus_World);
					Check_Next_Move_Validity(I, J-1, Wumpus_World);
					Check_Next_Move_Validity(I+1, J, Wumpus_World);
					Check_Next_Move_Validity(I-1, J, Wumpus_World);
					Wumpus_World[I][J].Visited = true;
					indx -= 1;
					P = Memory[indx];
					Memory[indx] = -1;
					I = P/4;
					J = P - (4*I);
					Wumpus_World[I][J].Safe = true;
					score.AnyAction += -1;
	    		}
			}
			
	    	else if(Wumpus_World[I][J].Safe==true && Wumpus_World[I][J].Breeze==true && Wumpus_World[I][J].Stench==true)
			{
	    		System.out.println("\n\n-------> Agent is Located in Cell : "+((I*4)+J)+" <-------");
	    		Traversed_Path[IDX++] = (I*4)+J;
	    		System.out.println("# Position Safe - Both Breeze - And Stench");
	    		System.out.println("# Lets Use an Arrow");
	    		Check_Next_Move_Validity(I, J, Wumpus_World);
	    		Wumpus_World[I][J].Visited = true;
				P = Traversed_Path[IDX-2];
				I = P/4;
				J = P - (4*I);
				Wumpus_World[I][J].Safe = true;
				score.AnyAction += -1;
			}
	    	
	    	else if(Wumpus_World[I][J].Safe==true && Wumpus_World[I][J].Breeze==true && Wumpus_World[I][J].Stench==false)
			{
	    		System.out.println("\n\n-------> Agent is Located in Cell : "+((I*4)+J)+" <-------");
	    		Traversed_Path[IDX++] = (I*4)+J;
				System.out.println("# Position Safe - Only Breeze - No Stench");
				Check_Next_Move_Validity(I, J, Wumpus_World);
				Wumpus_World[I][J].Visited = true;
				if(indx>0)
				{
					indx-=1;
					P = Memory[indx];
					Memory[indx] = -1;
					I = P/4;
					J = P - (4*I);
					Wumpus_World[I][J].Safe = true;
					score.AnyAction += -2;
				}
				else
				{
					P = Get_Next_Move();
					if(P==99)
					{
						System.out.println("\nAgent BackTracks...");
						P = Traversed_Path[IDX-2];
						I = P/4;
						J = P - (4*I);
						Wumpus_World[I][J].Safe = true;
						score.AnyAction += -1;
						P = DoMovement(I,J,Wumpus_World);
						I = P/4;
						J = P - (4*I);
						Wumpus_World[I][J].Safe = true;
						score.AnyAction += -1;
					}
					else
					{
						I = P/4;
						J = P - (4*I);
						Wumpus_World[I][J].Safe = true;
						score.AnyAction += -1;
					}
				}
			}
	    	
	    	else if(Wumpus_World[I][J].Safe==true && Wumpus_World[I][J].Breeze==false && Wumpus_World[I][J].Stench==true)
			{
	    		System.out.println("\n\n-------> Agent is Located in Cell : "+((I*4)+J)+" <-------");
	    		Traversed_Path[IDX++] = (I*4)+J;
				System.out.println("# Position Safe - No Breeze - Only Stench");
				Check_Next_Move_Validity(I, J, Wumpus_World);
				Wumpus_World[I][J].Visited = true;
				if(indx>0)
				{
					indx-=1;
					P = Memory[indx];
					Memory[indx] = -1;
					I = P/4;
					J = P - (4*I);
					Wumpus_World[I][J].Safe = true;
					score.AnyAction += -2;
				}
				else
				{
					P = Get_Next_Move();
					if(P==99)
					{
						System.out.println("\nAgent BackTracks...");
						P = Traversed_Path[IDX-2];
						I = P/4;
						J = P - (4*I);
						Wumpus_World[I][J].Safe = true;
						score.AnyAction += -1;
						P = DoMovement(I,J,Wumpus_World);
						I = P/4;
						J = P - (4*I);
						Wumpus_World[I][J].Safe = true;
						score.AnyAction += -1;
					}
					else
					{
						I = P/4;
						J = P - (4*I);
						Wumpus_World[I][J].Safe = true;
						score.AnyAction += -1;
					}
				}
			}
	    }
		return S;
	}
	

	




	public static void main(String[] args)throws IOException
	{
		System.out.println("\n\n************************************WUMPUS-WORLD-GAME************************************\n\n");
		
		Scanner sc = new Scanner(System.in);
		int Pits[] = new int[3];
		char type;
		
		Sensors Wumpus_World[][] = new Sensors[4][4];
		
		System.out.println("\nThe Wumpus World Is As Follows : ");
		Initialize_Wumpus_World_Game_Array(Wumpus_World);
		Display_Wumpus_World_Tile_Count();
		
		
		System.out.println("\n\nAgent Starts from Position : 12");
		Wumpus_World[3][0].Type = "A";
		Wumpus_World[3][0].Safe = true;
		Wumpus_World[3][0].Unsafe = false;
		Wumpus_World[3][0].Visited = false;
		Wumpus_World[3][0].Breeze = false;
		Wumpus_World[3][0].Bump = false;
		Wumpus_World[3][0].Glitter = false;
		Wumpus_World[3][0].Scream = false;
		Wumpus_World[3][0].Stench = false;
		
		
		System.out.print("\nEnter the No. of Pits : ");
		int n = sc.nextInt();
		
		
		System.out.print("\n\n***NOTE : The Position of Pits, Gold and Wumpus Must Not Overlap***\n\n");
		System.out.println("\n---> Enter Pit Positions (Except Start Position) <---");
		for(int i=0;i<n;i++)
		{
			System.out.print("Enter Pos : ");
			pit_pos = sc.nextInt();
			Pits[i] = pit_pos;
			type = 'P';
			Insert_into_Wumpus_World(type, pit_pos, Wumpus_World);
		}
		
		
		System.out.println("\n\n---> Enter Wumpus Position (Except Start Position) <---");
		System.out.print("Enter Pos : ");
		Wumpus_Pos = sc.nextInt();
		type = 'W';
		Insert_into_Wumpus_World(type, Wumpus_Pos, Wumpus_World);
		
		
		System.out.println("\n\n---> Enter Gold Position (Except Pit and Wumpus Position) <---");
		System.out.print("Enter Pos : ");
		Gold_Pos = sc.nextInt();
		type = 'G';
		Insert_into_Wumpus_World(type, Gold_Pos, Wumpus_World);
		
		
		//Display the Environment after Input
		Display_Wumpus_World_Game_Array(Wumpus_World);
		Display_Sensors_in_Every_Tile(Wumpus_World);
		
		
		//Play the Game
		System.out.println("\n\n\n#~~~~~~~~~~~~~~~~~~~~~~~~~~~LET'S PLAY~~~~~~~~~~~~~~~~~~~~~~~~~~~#\n\n");
		Init_Memory_Array();
		//System.out.println("---> Agent is Located in Cell : "+((I*4)+J)+"\n");
		int FinalScore = Play_Wumpus_World_Game(Wumpus_World);
		
		
		System.out.println("\nTHE PATH TRAVERSED : ");
		for(int m=0;m<IDX;m++)
		{
			System.out.print(Traversed_Path[m]+"->");
		}
		//Total Score
		System.out.println("\n\n**********PLAYER WINS! SCORE : "+FinalScore+"**********\n\n");
	}
}
