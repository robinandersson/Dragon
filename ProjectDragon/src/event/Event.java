package event;

/**
 * A single event
 * @author hajo
 *
 */
public class Event {
    // All possible events listed
    public enum Tag {
    	TRY_LOGIN, //TODO This event needs to be caught and the contents compared with the DB, send either LOGIN_SUCCESS or LOGIN_FAILED back
    	LOGIN_FAILED,
    	LOGIN_SUCCESS,
    	LOGOUT,
    	GO_TO_REGISTER,
    	GO_TO_JOINTABLE,
    	GO_TO_CREATETABLE,
    	GO_TO_STATISTICS,
    	REGISTER_BACK,
    	JOINTABLE_BACK,
    	CREATETABLE_BACK,
    	STATISTICS_BACK,
    	TRY_REGISTER,
    	REGISTER_FAILED,
    	REGISTER_SUCCESS,
    	CREATE_TABLE,
    	JOIN_TABLE,
    	MAKE_BET,
    	SHOWDOWN_DONE, //ej mottaget eller publicerat n�gonstans
    	SERVER_FOLD,
    	SERVER_UPDATE_BET,
    	SERVER_NEXT_TURN,
    	SERVER_DISTRIBUTE_CARDS,
    	SERVER_ADD_TABLE_CARDS,
    	SERVER_CLEAR_TABLE_CARDS,
    	SERVER_CREATE_TABLES
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
