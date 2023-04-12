import Fluent
import Vapor

final class Todo: Model, Content {
    static let schema = "todos"

    @ID(custom: "id")
    var id: Int?

    @Field(key: "body")
    var body: String

    init() { }

    init(id: Int? = nil, body: String) {
        self.id = id
        self.body = body
    }
}
