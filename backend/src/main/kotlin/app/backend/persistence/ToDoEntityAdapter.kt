package app.backend.persistence

import app.model.Completed
import app.model.ToDo
import app.model.ToDoStatus
import app.model.Uncompleted
import com.soywiz.klock.annotations.KlockExperimental
import com.soywiz.klock.wrapped.WDateTime

/**
 * Extension methods to adapt between the shared app.model, and the persistence entities
 */

@OptIn(KlockExperimental::class)
fun ToDoEntity.toModel() = when(status) {
    ToDoStatusEntity.CompletedStatus -> {
        ToDo(
            id.toString(),
            text,
            Completed(WDateTime(completedDate!!))
        )
    }
    ToDoStatusEntity.UncompletedStatus -> ToDo(id.toString(), text, Uncompleted)
}

fun ToDoStatus.toStatusEntity() = when(this) {
    is Completed -> ToDoStatusEntity.CompletedStatus
    Uncompleted -> ToDoStatusEntity.UncompletedStatus
}

@OptIn(KlockExperimental::class)
fun ToDoStatus.toCompletedDate() = when(this) {
    is Completed -> this.completedOn.unixMillisLong
    Uncompleted -> null
}

fun ToDo.toEntity() = ToDoEntity(
    this.id.toLong(),
    this.text,
    this.status.toStatusEntity(),
    this.status.toCompletedDate()
)