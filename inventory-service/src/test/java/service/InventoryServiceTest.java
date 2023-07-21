package service;

import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import com.example.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@ContextConfiguration
@AutoConfigureTestEntityManager
@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {

    //Isolate the service layer from the database access and focus on testing the business logic independently
    @MockBean
    private InventoryRepository inventoryRepository;

    //Automatically inject mock objects into a class under test
    @InjectMocks
    private InventoryService inventoryService;

    @Test
    public void testIsInStock() {
        // Mock the data from the repository
        Inventory inventory1 = new Inventory(1L, "sku001", 10);
        Inventory inventory2 = new Inventory(2L, "sku002", 0);

        /*
        We are telling Mockito that when the findBySkuCodeIn method of the inventoryRepository is called with the argument Arrays.asList("sku001", "sku002"),
        it should return a list containing inventory1 and inventory2.
         */
        when(inventoryRepository.findBySkuCodeIn(Arrays.asList("sku001", "sku002")))
                .thenReturn(Arrays.asList(inventory1, inventory2));

        // Call the service method
        List<InventoryResponse> responseList = inventoryService.isInStock(Arrays.asList("sku001", "sku002"));

        // Verify the results
        //Since at line 39 and 40 , I create 2 inventory object , so the answer should equal 2
        assertEquals(2, responseList.size());
        assertNotEquals(4, responseList.size());
        assertEquals("sku001", responseList.get(0).getSkuCode());
        assertEquals(true, responseList.get(0).isInStock());
        assertEquals("sku002", responseList.get(1).getSkuCode());
        assertEquals(false, responseList.get(1).isInStock());
    }
}