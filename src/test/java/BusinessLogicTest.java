import com.piotrnowicki.spikes.boundary.MergeBean;
import com.piotrnowicki.spikes.boundary.PersistBean;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

@RunWith(Arquillian.class)
public class BusinessLogicTest {

    @Inject
    PersistBean persistBean;

    @Inject
    MergeBean mergeBean;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "com.piotrnowicki.spikes").addAsManifestResource("META-INF/persistence.xml", "persistence.xml").addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void shouldPersistEntity() {
        persistBean.persistEntity();
    }

    @Test
    public void shouldMergeEntity() {
        mergeBean.mergeEntity();;
    }
}
