const API_BASE_URL = 'http://localhost:5000/api';

export const apiService = {
  async getLegacyClients() {
    try {
      const response = await fetch(`${API_BASE_URL}/legacy/clients`);
      if (!response.ok) {
        throw new Error('Failed to fetch legacy clients');
      }
      return await response.json();
    } catch (error) {
      console.error('Error fetching legacy clients:', error);
      throw error;
    }
  },

  async getNewClients() {
    try {
      const response = await fetch(`${API_BASE_URL}/new/clients`);
      if (!response.ok) {
        throw new Error('Failed to fetch new clients');
      }
      return await response.json();
    } catch (error) {
      console.error('Error fetching new clients:', error);
      throw error;
    }
  },

  async migrateClient(id) {
    try {
      const response = await fetch(`${API_BASE_URL}/migrate/${id}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      if (!response.ok) {
        const errorData = await response.json().catch(() => ({ message: 'Migration failed' }));
        throw new Error(errorData.message || 'Failed to migrate client');
      }
      return await response.json();
    } catch (error) {
      console.error('Error migrating client:', error);
      throw error;
    }
  },
};

