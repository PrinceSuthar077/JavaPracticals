class FileResource implements AutoCloseable {

    FileResource() {
        System.out.println("Resource opened");
    }

    void read() throws Exception {
        System.out.println("Reading resource");
        throw new Exception("Error while reading");
    }

    public void close() {
        System.out.println("Resource closed");
    }
}

public class Main {
    public static void main(String[] args) {

        try (FileResource resource = new FileResource()) {

            resource.read();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
