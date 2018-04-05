package cc.upedu.online.view.wheelcity;

public class AddressBean {
	private int id;
	private int parent_id;
	private String area_name;
	private int area_type;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getParent_id() {
		return parent_id;
	}
	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}
	public String getArea_name() {
		return area_name;
	}
	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}
	public int getArea_type() {
		return area_type;
	}
	public void setArea_type(int area_type) {
		this.area_type = area_type;
	}
	@Override
	public String toString() {
		return "AddressBean [id=" + id + ", parent_id=" + parent_id
				+ ", area_name=" + area_name + ", area_type=" + area_type + "]";
	}
	
}
