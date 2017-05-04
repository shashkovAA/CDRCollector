package objects;

/**
 * Класс используется как тип данных для объекта, который помещается в
 * наблюдаемый ArrayList в поле ImageType. Нужен для хранения строкового
 * значения имени файла иконки, которая соответствует типу вызова, и для
 * отображения иконки в таблице вызовов.
 */
public class ImageForCallType

{
    /** Имя графического файла, отображающий тип вызова */
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
