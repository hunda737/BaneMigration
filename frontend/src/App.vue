<script setup>
import { ref, onMounted } from 'vue';
import { apiService } from './services/api';

const legacyClients = ref([]);
const newClients = ref([]);
const loading = ref(false);
const snackbar = ref(false);
const snackbarText = ref('');
const snackbarColor = ref('success');

const legacyHeaders = [
  { title: 'ID', key: 'id', align: 'start' },
  { title: 'Name', key: 'fullName' },
  { title: 'Email', key: 'email' },
  { title: 'Phone', key: 'phoneNumber' },
  { title: 'Segment', key: 'segment' },
  { title: 'Registered At', key: 'registeredAt' },
  { title: 'Status', key: 'migrated' },
  { title: 'Actions', key: 'actions', sortable: false },
];

const newHeaders = [
  { title: 'ID', key: 'id', align: 'start' },
  { title: 'Name', key: 'fullName' },
  { title: 'Email', key: 'email' },
  { title: 'Phone', key: 'phoneNumber' },
  { title: 'Segment', key: 'segment' },
  { title: 'Migrated At', key: 'updatedAt' },
];

const fetchData = async () => {
  loading.value = true;
  try {
    const [legacy, newData] = await Promise.all([
      apiService.getLegacyClients(),
      apiService.getNewClients(),
    ]);
    legacyClients.value = legacy;
    newClients.value = newData;
  } catch (error) {
    showSnackbar('Error loading data: ' + error.message, 'error');
  } finally {
    loading.value = false;
  }
};

const migrateClient = async (client) => {
  loading.value = true;
  try {
    await apiService.migrateClient(client.id);
    const clientLabel = client?.fullName || client?.name || client?.email || client?.id;
    showSnackbar(`Client ${clientLabel} migrated successfully!`, 'success');
    await fetchData();
  } catch (error) {
    showSnackbar('Migration failed: ' + error.message, 'error');
  } finally {
    loading.value = false;
  }
};

const showSnackbar = (text, color = 'success') => {
  snackbarText.value = text;
  snackbarColor.value = color;
  snackbar.value = true;
};

const getStatusColor = (migrated) => {
  return migrated ? 'success' : 'warning';
};

const getStatusText = (migrated) => {
  return migrated ? 'Migrated' : 'Pending';
};

const formatDate = (value) => {
  if (!value) return 'N/A';
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return 'Invalid date';
  return date.toLocaleString();
};

onMounted(() => {
  fetchData();
});
</script>

<template>
  <v-app>
    <v-app-bar color="primary" prominent>
      <v-app-bar-title class="text-h4 font-weight-bold">
        Bane - Client Migration Tool
      </v-app-bar-title>
    </v-app-bar>

    <v-main class="bg-gray-50">
      <v-container fluid class="pa-6">
        <!-- Legacy Clients Section -->
        <v-card class="mb-8 shadow-lg">
          <v-sheet
            class="px-6 py-4 d-flex align-center text-white gap-2"
            style="background-color: #2563eb;"
          >
            <v-icon icon="mdi-database" color="white"></v-icon>
            <span class="text-h5 font-weight-bold">Legacy Clients</span>
          </v-sheet>
          <v-card-text class="pa-0">
            <v-data-table
              :headers="legacyHeaders"
              :items="legacyClients"
              :loading="loading"
              class="elevation-0"
              items-per-page="10"
            >
              <template v-slot:item.registeredAt="{ item }">
                <span class="text-gray-600">
                  {{ formatDate(item.registeredAt) }}
                </span>
              </template>
              <template v-slot:item.migrated="{ item }">
                <v-chip
                  :color="getStatusColor(item.migrated)"
                  size="small"
                  variant="flat"
                >
                  {{ getStatusText(item.migrated) }}
                </v-chip>
              </template>
              <template v-slot:item.actions="{ item }">
                <v-btn
                  color="primary"
                  variant="elevated"
                  size="small"
                  :disabled="item.migrated || loading"
                  @click="migrateClient(item)"
                  class="font-weight-bold"
                >
                  <v-icon icon="mdi-arrow-right-bold" class="mr-1"></v-icon>
                  Migrate
                </v-btn>
              </template>
            </v-data-table>
          </v-card-text>
        </v-card>

        <!-- New System Clients Section -->
        <v-card class="shadow-lg">
          <v-sheet
            class="px-6 py-4 d-flex align-center text-white gap-2"
            style="background-color: #16a34a;"
          >
            <v-icon icon="mdi-cloud-check" color="white"></v-icon>
            <span class="text-h5 font-weight-bold">Migrated Clients (New System)</span>
          </v-sheet>
          <v-card-text class="pa-0">
            <v-data-table
              :headers="newHeaders"
              :items="newClients"
              :loading="loading"
              class="elevation-0"
              items-per-page="10"
            >
              <template v-slot:item.updatedAt="{ item }">
                <span class="text-gray-600">
                  {{ formatDate(item.updatedAt) }}
                </span>
              </template>
            </v-data-table>
          </v-card-text>
        </v-card>
      </v-container>
    </v-main>

    <!-- Snackbar for notifications -->
    <v-snackbar
      v-model="snackbar"
      :color="snackbarColor"
      :timeout="3000"
      location="top"
    >
      {{ snackbarText }}
      <template v-slot:actions>
        <v-btn
          variant="text"
          @click="snackbar = false"
        >
          Close
        </v-btn>
      </template>
    </v-snackbar>
  </v-app>
</template>

<style scoped>
/* Additional custom styles can be added here if needed */
</style>
