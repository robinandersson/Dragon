package event;

/**
 * A single event
 * @author hajo
 *
 */
public class Event {
    // All possible events listed
    public enum Tag {
    	MAKE_BET,
    	SHOWDOWN_DONE, //ej mottaget eller publicerat n�gonstans
    	SERVER_FOLD,		//getValue(): iPlayer
    	SERVER_UPDATE_BET,	//getValue(): Bet
    	SERVER_NEXT_TURN,	//getValue(): iPlayer
    	SERVER_DISTRIBUTE_CARDS,//getValue(): Map<iPlayer, iHand>
    	SERVER_ADD_TABLE_CARDS,	//getValue(): List<iCard>
    	SERVER_CREATE_TABLE,
    	SERVER_DISTRIBUTE_POT,
    	SERVER_UPDATE_POT,
    	SERVER_NEW_ROUND,
    	SERVER_SET_TURN,
    	SERVER_SET_PLAYER_UNACTIVE,
    	SERVER_SET_OWN_CURRENT_BET
    }
    private final Tag tag;
    // The new value 
    private final Object value;
    public Event(Tag tag, Object value){
        this.tag = tag;
        this.value = value;
    }
    public Tag getTag() {
        return tag;
    }
    public Object getValue() {
        return value;
    }
    @Override
    public String toString() {
        return "Event [tag=" + tag + ", value=" + value + "]";
    } 
    
    
}
