package app.backend.data

import app.model.Completed
import app.model.ToDoStatus
import app.model.Uncompleted
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.LongNode
import com.fasterxml.jackson.databind.node.TextNode
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.soywiz.klock.wrapped.WDateTime

val ToDoModule = SimpleModule()
    .also { it.addSerializer(ToDoStatus::class.java, ToDoSerializer()) }
    .also { it.addDeserializer(ToDoStatus::class.java, ToDoDeserializer()) }

class ToDoSerializer : StdSerializer<ToDoStatus>(ToDoStatus::class.java) {
    override fun serialize(value: ToDoStatus, gen: JsonGenerator, provider: SerializerProvider) {
        when (value) {
            is Completed -> {
                gen.writeStartObject()
                gen.writeStringField("type", Completed::class.qualifiedName)
                gen.writeNumberField("completedOn", value.completedOn.unixMillisLong)
                gen.writeEndObject()
            }
            is Uncompleted -> {
                gen.writeStartObject()
                gen.writeStringField("type", Uncompleted::class.qualifiedName)
                gen.writeEndObject()
            }
        }
    }
}

class ToDoDeserializer : StdDeserializer<ToDoStatus>(ToDoStatus::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ToDoStatus {
        val readTree: TreeNode = p.codec.readTree(p)
        val type = (readTree.get("type") as TextNode).textValue()

        return when (type) {
            Completed::class.qualifiedName -> Completed(WDateTime.fromUnix((readTree.get("completedOn") as LongNode).longValue()))
            Uncompleted::class.qualifiedName -> Uncompleted
            else -> throw IllegalArgumentException()
        }
    }
}