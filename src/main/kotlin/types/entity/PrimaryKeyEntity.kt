import com.github.f4b6a3.ulid.UlidCreator
import jakarta.persistence.Column
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PostLoad
import jakarta.persistence.PostPersist
import org.hibernate.proxy.HibernateProxy
import org.springframework.data.domain.Persistable
import java.util.Objects

@MappedSuperclass
abstract class PrimaryKeyEntity: Persistable<String> {
    @Transient
    private var _isNew = true

    override fun isNew(): Boolean = _isNew

    override fun equals(other: Any?): Boolean {
        if (other == null) return false

        if (other !is HibernateProxy && this::class != other::class) return false

        return id == getIdentifier(other)
    }

    private fun getIdentifier(obj: Any): String? {
        return if (obj is HibernateProxy) {
            (obj.hibernateLazyInitializer.implementation as PrimaryKeyEntity).id
        } else {
            (obj as PrimaryKeyEntity).id
        }
    }

    override fun hashCode() = Objects.hashCode(id)

    @PostPersist
    @PostLoad
    protected fun load() {
        _isNew = false
    }
}