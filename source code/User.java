import java.util.ArrayList;

public class User{
		
		public int UserId;
		public int rate;
		int index;
		int reputation;
		boolean insert=false;
		ArrayList<Edge> newUsers=new ArrayList<Edge>();
		public void addFriend(int friendId,int weight){
			
			for(Edge temp:newUsers){
				if (temp.getFriendId()==friendId){
					temp.setWeight(weight+this.rate);
					insert=true;
				}}
		 
			if(!insert){
			Edge newEdge=new Edge(friendId,weight);
			this.newUsers.add(newEdge);
			}
		}
		public User(int user){
			this.UserId=user;
			this.rate=0;
		}
		public void setReputation(int reputation)
		{
			this.reputation=reputation;
		}
		public int getNumOfFriends(){
			return newUsers.size();
		}
		public boolean FriendsListEmpty(){
			if(newUsers.isEmpty())return true;
			else return false;
		}
		public int getIdOfFriend(int index){
			Edge temp=newUsers.get(index);
			return temp.getFriendId();
				
			}
		public int userHasFriend(int idOfFriend)	{
			for(Edge friend:newUsers){
				if(friend.friendId==idOfFriend)return newUsers.indexOf(friend);
				}
			return -1;
			}
		public void updateUserRate(int value){
			this.rate+=value;
		}
		public int getWeightOfFriend(int index){
			Edge temp=newUsers.get(index);
			return temp.getWeight();
		}
		public void addWeightToFriend(int friendIndex,int weight){
			Edge temp=newUsers.get(friendIndex);
			temp.setWeight(weight);
		}
}