package objects;

/**
 * ����� ������������ ��� ��� ������ ��� �������, ������� ���������� �
 * ����������� ArrayList � ���� ImageType. ����� ��� �������� ����������
 * �������� ����� ����� ������, ������� ������������� ���� ������, � ���
 * ����������� ������ � ������� �������.
 */
public class ImageForCallType

{
    /** ��� ������������ �����, ������������ ��� ������ */
    private String imageFileName;

    public ImageForCallType() {
    }

    public ImageForCallType(String imageType) {
	this.imageFileName = imageType;
    }

    public String getFileImageTypeName() {
	return imageFileName;
    }

    public void setFileImageTypeName(String imgType) {
	this.imageFileName = imgType;
    }

}
