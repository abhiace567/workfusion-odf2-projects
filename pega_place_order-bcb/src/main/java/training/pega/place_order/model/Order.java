package training.pega.place_order.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.workfusion.odf2.core.orm.Datastore;
import com.workfusion.odf2.core.orm.DatastoreType;
import com.workfusion.odf2.transaction.model.OdfTransactionalEntity;

@DatabaseTable(tableName = "orders")
@Datastore(type = DatastoreType.VERSIONED)
public class Order extends OdfTransactionalEntity {

	public static final String ITEM = "item";
	public static final String TYPE = "type";
	public static final String QUANTITY = "quantity";
	public static final String STATUS = "order_status";
	public static final String TOTAL_PRICE = "total_price";
	public static final String ORDER_ID = "order_id";

	@DatabaseField(columnName = ITEM, dataType = DataType.STRING)
	private String item;

	@DatabaseField(columnName = TYPE, dataType = DataType.STRING)
	private String type;

	@DatabaseField(columnName = QUANTITY, dataType = DataType.STRING)
	private String quantity;

	@DatabaseField(columnName = STATUS, dataType = DataType.STRING)
	private String status;

	@DatabaseField(columnName = TOTAL_PRICE, dataType = DataType.STRING)
	private String totalPrice;

	@DatabaseField(columnName = ORDER_ID, dataType = DataType.STRING)
	private String orderId;

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Override
	public String toString() {
		return "Order [item=" + item + ", type=" + type + ", quantity=" + quantity + ", status=" + status
				+ ", totalPrice=" + totalPrice + ", orderId=" + orderId + ", getTransaction()=" + getTransaction()
				+ ", getUuid()=" + getUuid() + "]";
	}

}
