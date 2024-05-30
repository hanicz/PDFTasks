package pdf.tasks.exception.dto;

public record ErrorResponse(
        int code,
        String error
) {
}
