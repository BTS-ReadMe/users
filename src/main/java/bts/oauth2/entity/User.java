package bts.oauth2.entity;

import bts.oauth2.util.BaseTimeEntity;
import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@ToString(exclude = "socialAuth")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user", uniqueConstraints = {
    @UniqueConstraint(columnNames = "email")
})
@SecondaryTables({
    @SecondaryTable(name = "social_auth", pkJoinColumns = @PrimaryKeyJoinColumn(name = "user_id"))
})
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    private String phone;
    private Integer age;
    private String gender;
    private Integer point;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "providerId", column = @Column(table = "social_auth", name = "provider_id")),
        @AttributeOverride(name = "provider", column = @Column(table = "social_auth", name = "provider")),
        @AttributeOverride(name = "email", column = @Column(table = "social_auth", name = "email", nullable = false)),
        @AttributeOverride(name = "name", column = @Column(table = "social_auth", name = "name", nullable = false)),
        @AttributeOverride(name = "attributes", column = @Column(table = "social_auth", name = "attributes", columnDefinition = "TEXT")),
        @AttributeOverride(name = "ip", column = @Column(table = "social_auth", name = "ip", nullable = false)),
    })
    private SocialAuth socialAuth;
}
